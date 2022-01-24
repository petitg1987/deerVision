import React, {Component} from 'react';
import "../pages.css"
import "./privacy.css"
import Description from "../../components/description/description";

class PrivacyPolicy extends Component {

    render() {
        return (
            <div>
                <h2>Games privacy policy</h2>
                <div className="pp-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="pp-description">
                        <Description>
                            <div className={"sub-title"}>Types of information collected:</div>
                            <div className={"sub-category"}>Personal information:</div>
                            <p>
                                Definition: personal information is information that would allow to identify or contact you, including, for example, your full name, address, telephone number, or email address.
                                <br/><br/>
                                Personal information collected:
                                <ul>
                                    <li><i>None</i></li>
                                </ul>
                            </p>
                            <div className={"sub-category"}>Non-personal information:</div>
                            <p>
                                Definition: non-personal information means information that cannot be used to identify a specific user.
                                <br/><br/>
                                Non-personal information collected:
                                <ul>
                                    <li>Game usage statistics</li>
                                    <li>Crash logs (including: graphics card name, CPU name, total memory and operating system name)</li>
                                </ul>
                            </p>

                            <div className={"sub-title"}>How information collected becomes in use:</div>
                            <p>The company may use and collect non-personal information to provide and improve the service. The company reserves the right to process non-personal information collected from users. These may include:
                                <ul>
                                    <li>Tracking usage patterns and analyzing trends of users</li>
                                    <li>Technical problem resolution</li>
                                    <li>Searching user statistics and provision of leaderboard</li>
                                </ul>
                            </p>

                            <div className={"sub-title"}>Retention of your data:</div>
                            <p>The company will retain your information only for as long as is necessary for the purposes set out in this privacy policy.</p>


                            <div className={"sub-title"}>How we protect information collected:</div>
                            <p>The company is committed to provide safeguards with the information collected to ensure that the information is not lost, stolen, exposed, altered or damaged. However, it is difficult to guarantee complete protection of all information. Hence, you should acknowledge that complete protection of information cannot be guaranteed if you choose to use our services.</p>

                            <div className={"sub-title"}>Who has access to the information:</div>
                            <p>Only the employees of the company can access to the collected information. The information is not shared with third parties.</p>

                            <div className={"sub-title"}>Your rights:</div>
                            <p>
                                As there is no personal information collected, the personal information removal request is meaningless.
                                <br/>
                                All our games provide settings to disable the collection of no-personal information.
                            </p>
                        </Description>
                    </div>
                </div>

                <h2>Website privacy policy</h2>
                <div>
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div>
                        <Description>
                            <p>This website does not collect any data and do not use cookies.</p>
                        </Description>
                    </div>
                </div>
            </div>)
    }
}

export default PrivacyPolicy;
