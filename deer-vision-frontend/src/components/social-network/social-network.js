import React, {Component} from 'react';
import discordLogo from "../../images/discordLogo.webp";
import youtubeLogo from "../../images/youtubeLogo.webp";
import xLogo from "../../images/xLogo.webp";
import './social-network.css';
import uniqueId from 'lodash/uniqueId'

class SocialNetwork extends Component {

    constructor(props) {
        super(props);
        this.networkId = uniqueId('network-');
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
        observer.observe(document.querySelector('#' + this.networkId));
    }

    render() {
        let labelComponent;
        if(this.props.label && this.props.label !== "") {
            labelComponent = <div className="network-title">{this.props.label}</div>
        }

        return (
            <div className="network-container">
                {labelComponent}
                <div className="network-links" id={this.networkId}>
                    <a href="https://discord.gg/bRMXVejHmU" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                        <img className="network-logo" src={discordLogo} alt="Discord Logo" width={this.props.logoSize} height={this.props.logoSize}/>
                    </a>
                    <a href="https://twitter.com/DeerVisionStd" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                        <img className="network-logo" src={xLogo} alt="X Logo" width={this.props.logoSize} height={this.props.logoSize}/>
                    </a>
                    <a href="https://www.youtube.com/channel/UC1MNRfIXqQPB8dQtU94PpdQ" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                        <img className="network-logo" src={youtubeLogo} alt="Youtube Logo" width={this.props.logoSize} height={this.props.logoSize}/>
                    </a>
                </div>
            </div>
        );
    }
}

export default SocialNetwork;