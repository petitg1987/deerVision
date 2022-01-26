import React, {Component} from 'react';
import "../pages.css"
import "./privacy.css"
import Description from "../../components/description/description";

class Privacy extends Component {

    render() {
        return (
            <div>
                <h2>Privacy Policy: Games</h2>
                <div className="pp-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="pp-description">
                        <Description>
                            <p>Last update: 24th of January 2022</p>
                        </Description>

                        <div className={"sub-title"}>Definitions</div>
                        <Description>
                            <p>For the purposes of this Privacy Policy:</p>
                            <ul>
                                <li><strong>Company</strong> (referred to as either "Company", "We", "Us" or "Our" in this Agreement) refers to Deer Vision Studio.</li>
                            </ul>
                        </Description>

                        <div className={"sub-title"}>Types of information collected</div>
                        <div className={"sub-category"}>Personal information:</div>
                        <Description>
                            <p>
                                Personal information is information that would allow identifying or contact you, including, for example, your full name, address, telephone number, IP address, or email address.
                                <br/><br/>
                                Personal information collected:
                            </p>
                            <ul>
                                <li><i>None</i></li>
                            </ul>
                        </Description>

                        <div className={"sub-category"}>Non-personal information:</div>
                        <Description>
                            <p>
                                Non-personal information means information that cannot be used to identify a specific user.
                                <br/><br/>
                                Non-personal information collected:
                            </p>
                            <ul>
                                <li>Game usage statistics</li>
                                <li>Crash logs (including: game logs, game settings, graphics card name, CPU name, total memory and operating system name)</li>
                            </ul>
                        </Description>

                        <div className={"sub-title"}>How information collected becomes in use</div>
                        <Description>
                            <p>The Company may use and collect non-personal information to provide and improve the service. The Company reserves the right to process non-personal information collected from users. These may include:</p>
                            <ul>
                                <li>Tracking usage patterns and analyzing trends of users</li>
                                <li>Technical problem resolution</li>
                                <li>Searching user statistics and provision of leaderboard</li>
                            </ul>
                        </Description>

                        <div className={"sub-title"}>Retention of your data</div>
                        <Description>
                            <p>The Company will retain your information only for as long as is necessary for the purposes set out in this privacy policy.</p>
                        </Description>

                        <div className={"sub-title"}>How we protect information collected</div>
                        <Description>
                            <p>The Company is committed to provide safeguards with the information collected to ensure that the information is not lost, stolen, exposed, altered or damaged. However, it is difficult to guarantee complete protection of all information. Hence, you should acknowledge that complete protection of information cannot be guaranteed if you choose to use our services.</p>
                        </Description>

                        <div className={"sub-title"}>Who has access to the information</div>
                        <Description>
                            <p>Only the Company can access to the collected information. The information is not shared with third parties.</p>
                        </Description>

                        <div className={"sub-title"}>Your rights</div>
                        <Description>
                            <p>
                                As there is no personal information collected, the personal information removal request is meaningless.
                                <br/>
                                All our games provide settings to disable the collection of no-personal information.
                            </p>
                        </Description>

                        <div className={"sub-title"}>Amendments</div>
                        <Description>
                            <p>We reserve the right to modify this privacy policy at any time, so please review it frequently. If we make material changes to this policy, we will also revise the "last update" date at the top of this privacy policy. Your continued use of our games will signify your acceptance of the changes to our privacy policy.</p>
                        </Description>

                        <div className={"sub-title"}>Contact</div>
                        <Description>
                            <p>For any questions relating to the privacy policy, please contact us at <a className={"text-link"} href={"mailto:contact@deervision.studio"}>contact@deervision.studio</a></p>
                        </Description>
                    </div>
                </div>

                <h2>Privacy Policy: Website</h2>
                <div className="pp-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="pp-description">
                        <Description>
                            <p>This website does not collect any data and do not use cookies.</p>
                        </Description>
                    </div>
                </div>
            </div>)
    }
}

export default Privacy;
