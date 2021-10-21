import React, {Component} from 'react';
import discordLogo from "../../images/discordLogo.png";
import youtubeLogo from "../../images/youtubeLogo.png";
import reeditLogo from "../../images/reeditLogo.png";
import twitterLogo from "../../images/twitterLogo.png";
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

        let reeditComponent;
        if (this.props.reeditGameUrl && this.props.reeditGameUrl !== "") {
            reeditComponent = <a href={this.props.reeditGameUrl} target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                <img className="network-logo" src={reeditLogo} alt="Reedit Logo" width={this.props.logoSize} height={this.props.logoSize}/>
            </a>;
        }

        let noCommunityComponents;
        if (!this.props.onlyCommunityNetwork) {
            noCommunityComponents = <a href="https://www.youtube.com/channel/UC1MNRfIXqQPB8dQtU94PpdQ" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                <img className="network-logo" src={youtubeLogo} alt="Youtube Logo" width={this.props.logoSize} height={this.props.logoSize}/>
            </a>;
        }

        return (
            <div className="network-container">
                {labelComponent}
                <div className="network-links" id={this.networkId}>
                    <a href="https://discord.gg/bRMXVejHmU" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                        <img className="network-logo" src={discordLogo} alt="Discord Logo" width={this.props.logoSize} height={this.props.logoSize}/>
                    </a>
                    <a href="https://twitter.com/deer_vision" target="_blank" rel="noopener noreferrer" className="network-logo-wrapper">
                        <img className="network-logo" src={twitterLogo} alt="Twitter Logo" width={this.props.logoSize} height={this.props.logoSize}/>
                    </a>
                    {reeditComponent}
                    {noCommunityComponents}
                </div>
            </div>
        );
    }
}

export default SocialNetwork;