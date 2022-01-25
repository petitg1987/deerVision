import React, {Component} from 'react';
import "../pages.css"
import "./eula.css"
import Description from "../../components/description/description";
import SocialNetwork from "../../components/social-network/social-network";

class Eula extends Component {

    render() {
        return (
            <div>
                <h2>End User License Agreement: Games</h2>
                <div className="eula-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="eula-description">
                        <Description>
                            <p>Last update: 24th of January 2022</p>
                        </Description>

                        <div className={"sub-title"}>Acknowledgment:</div>
                        <Description>
                            <p>
                                By purchasing, downloading or using the software product ("Software"), you agree to the terms of this Software Product License Agreement ("Agreement"). This Agreement is between Deer Vision Studio ("Company") and you. If you do not agree to the terms of this Agreement, do not purchase, download or use the Software. Please read this entire Agreement, which governs your use of the Software.
                                <br/><br/>
                                Your use of the Software is also subject to the <a className={"text-link"} href={"/privacy"}>Privacy Policy</a>.
                            </p>
                        </Description>

                        <div className={"sub-title"}>Restriction of use:</div>
                        <Description>
                            <p>You shall use the Software strictly in accordance with the terms of the related Agreement and shall not:</p>
                            <ul>
                                <li>(a) sell, rent, lease, assign, distribute, transmit, host, outsource, disclose or otherwise commercially exploit the Software or make the Software available to any third party</li>
                                <li>(b) violate any applicable laws, rules or regulations in connection with your access or use of the Software;</li>
                                <li>(c) remove, alter or obscure any proprietary notice (including any notice of copyright or trademark) of Company;</li>
                                <li>(d) use the Software for any revenue generating endeavor, commercial enterprise, or other purpose for which it is not designed or intended;</li>
                                <li>(e) install, use or permit the Software to exist on more than one device at a time;</li>
                                <li>(f) distribute the Software to multiple devices;</li>
                                <li>(g) use the Software for creating a product, service or software that is, directly or indirectly, competitive with or in any way a substitute for any services, product or software offered by Company;</li>
                            </ul>
                        </Description>

                        <div className={"sub-title"}>Internet connection:</div>
                        <Description>
                            <p>Some Software features may use an internet connection. You are responsible for all costs and fees charged by your internet service provider related to the download and use of the Software.</p>
                        </Description>

                        <div className={"sub-title"}>No warranties:</div>
                        <Description>
                            <p>The Software is provided to you "AS IS" and "AS AVAILABLE" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Software, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Software will meet your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.</p>
                        </Description>

                        <div className={"sub-title"}>Limitation of liability:</div>
                        <Description>
                            <p>To the maximum extent permitted by applicable law, in no event shall Company, be liable for any special, incidental, indirect, or consequential damages whatsoever (including, without limitation, damages for loss of business profits, business interruption, loss of business information, or any other pecuniary loss) arising out of the use of or inability to use the software product or the provision of or failure to provide support services, even if Company has been advised of the possibility of such damages. In any case, Company's entire liability under any provision of this Agreement shall be limited to the amount actually paid by you for the software product; provided however, if you have entered into a support services agreement, Company's entire liability regarding support services shall be governed by the terms of that Agreement. Notwithstanding the foregoing, some countries, states or other jurisdictions do not allow the exclusion of certain warranties or the limitation of liability as stated above, so the above terms may not apply to you. Instead, in such jurisdictions, the foregoing exclusions and limitations will apply to the maximum extent permitted by the laws of such jurisdictions.</p>
                        </Description>

                        <div className={"sub-title"}>Amendments:</div>
                        <Description>
                            <p>We reserve the right to modify this Agreement at any time, so please review it frequently. If we make material changes to this policy, we will also revise the "last update" date at the top of this Agreement. Your continued use of our Software will signify your acceptance of the changes to the Agreement.</p>
                        </Description>

                        <div className={"sub-title"}>Contact:</div>
                        <Description>
                            <p>For any questions relating to the Agreement, please contact us via private messages on one of these platforms:</p>
                        </Description>
                        <SocialNetwork logoSize={40} onlyCommunityNetwork={true}/>
                    </div>
                </div>
            </div>)
    }
}

export default Eula;
