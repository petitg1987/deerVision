package studio.deervision.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "binary")
public class BinaryProperties {

    private int chartDays;

    public int getChartDays() {
        return chartDays;
    }

    public void setChartDays(int chartDays) {
        this.chartDays = chartDays;
    }
}
