package studio.deervision.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import studio.deervision.model.Visitor;
import studio.deervision.repository.VisitorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorService.class);
    private static final String DEV_COUNTRY = "BE";
    private static final String UNKNOWN_COUNTRY = "Unknown";

    private final VisitorRepository visitorRepository;
    private final DatabaseReader geoDbReader;

    @Autowired
    public VisitorService(VisitorRepository visitorRepository) throws IOException {
        this.visitorRepository = visitorRepository;

        Resource geoDbResource = new ClassPathResource("GeoLite2-Country_20230811.mmdb"); //file from https://www.maxmind.com/en/account/login OR https://download.maxmind.com/app/geoip_download?edition_id=GeoLite2-Country&license_key=62Bcxf97UVQar0Ya&suffix=tar.gz
        geoDbReader = new DatabaseReader.Builder(geoDbResource.getInputStream()).build();
    }

    @Transactional
    public void addVisitor(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if ("127.0.0.1".equals(ipAddress)) {
            LOGGER.warn("Local IP address found (127.0.0.1). If you are in production: check Nginx (proxy_set_header) & Spring configuration (server.forward-headers-strategy)");
        }

        String visitorId = generateVisitorId(request);
        if (StringUtils.hasText(visitorId)) {
            boolean isVisitorKnown = visitorRepository.existsById(visitorId);
            if (!isVisitorKnown) {
                Optional<String> countryCode = getVisitorCountryCode(request);

                Visitor newVisitor = new Visitor();
                newVisitor.setId(visitorId);
                newVisitor.setVisitDate(LocalDate.now());
                countryCode.ifPresent(newVisitor::setCountryCode);
                visitorRepository.saveAndFlush(newVisitor);
            }
        }
    }

    public String getVisitorCountry(HttpServletRequest request) {
        Optional<String> countryCode = getVisitorCountryCode(request);
        return countryCode.orElse(UNKNOWN_COUNTRY);
    }

    public Map<LocalDate, Long> findVisitorCount(LocalDate fromDate) {
        List<Object[]> visitorsCountList = visitorRepository.findVisitorCount(fromDate);
        return visitorsCountList.stream().collect(Collectors.toMap(v -> (LocalDate)v[0], v -> (Long)v[1]));
    }

    public Map<String, Long> findVisitorCountByCountry(LocalDate fromDate) {
        List<Object[]> visitorsCountryList = visitorRepository.findVisitorCountByCountry(fromDate);
        return visitorsCountryList.stream().collect(Collectors.toMap(v -> (String)v[0], v -> (Long)v[1]));
    }

    private String generateVisitorId(HttpServletRequest request) {
        if (request != null) {
            String browserName = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            String visitorInfo = (ipAddress == null) ? "" : ipAddress;
            visitorInfo += (browserName == null) ? "" : browserName;
            if (!StringUtils.hasText(visitorInfo)) {
                LOGGER.warn("Visitor without information spotted");
                return null;
            }

            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(visitorInfo.getBytes());
                StringBuilder hexStr = new StringBuilder();
                for (byte b : messageDigest) {
                    hexStr.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
                }
                String md5 = hexStr.toString();
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                return currentDate + "_" + md5;
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Error to hash visitor id: " + e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    private Optional<String> getVisitorCountryCode(HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                LOGGER.info("Local IP address found (127.0.0.1). Use country: " + DEV_COUNTRY);
                return Optional.of(DEV_COUNTRY);
            }
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CountryResponse countryResponse = geoDbReader.country(inetAddress);
            return Optional.of(countryResponse.getCountry().getIsoCode());
        } catch(IOException | GeoIp2Exception e) {
            LOGGER.info("Impossible to retrieve visitor country code: " + e.getMessage());
        }
        return Optional.empty();
    }

}
