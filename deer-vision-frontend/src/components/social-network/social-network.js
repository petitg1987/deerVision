import React, {Component} from 'react';
import discordLogo from "../../images/discordLogo.png";
import youtubeLogo from "../../images/youtubeLogo.png";
import reeditLogo from "../../images/reeditLogo.png";
import twitterLogo from "../../images/twitterLogo.png";
import './social-network.css';

class SocialNetwork extends Component {

    constructor(props) {
        super(props);
        this.logoSize = 50;
    }

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                const networkLogos = entry.target.querySelectorAll('.network-logo');
                if (entry.isIntersecting) {
                    networkLogos.forEach(e => e.classList.add('network-logo-anim-trigger'));
                } else {
                    networkLogos.forEach(e => e.classList.remove('network-logo-anim-trigger'));
                }
            });
        });
        observer.observe(document.querySelector('.network-links'));
    }

    render() {
        return (
            <div className="network-container">
                <div className="network-title">Join us on:</div>
                <div className="network-links">
                    <a href="https://discord.com/invite/XYZ1234" target="_blank" rel="noopener noreferrer">
                        <div className="network-logo-wrapper">
                            <img className="network-logo" src={discordLogo} alt="Discord Logo" width={this.logoSize} height={this.logoSize}/>
                        </div>
                    </a>
                    <a href="https://www.twitter.com/deervisionstudio" target="_blank" rel="noopener noreferrer">
                        <div className="network-logo-wrapper">
                            <img className="network-logo" src={twitterLogo} alt="Twitter Logo" width={this.logoSize} height={this.logoSize}/>
                        </div>
                    </a>
                    <a href="https://www.reddit.com/r/deervisionstudio" target="_blank" rel="noopener noreferrer">
                        <div className="network-logo-wrapper">
                            <img className="network-logo" src={reeditLogo} alt="Reedit Logo" width={this.logoSize} height={this.logoSize}/>
                        </div>
                    </a>
                    <a href="https://www.youtube.com/c/DeerVisionStudio" target="_blank" rel="noopener noreferrer">
                        <div className="network-logo-wrapper">
                            <img className="network-logo" src={youtubeLogo} alt="Youtube Logo" width={this.logoSize} height={this.logoSize}/>
                        </div>
                    </a>
                </div>
            </div>
        );
    }
}

export default SocialNetwork;