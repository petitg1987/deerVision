import React, { Component } from 'react';
import "../pages.css"
import "./about.css"
import Description from "../../components/description/description";
import studioLogo from "../../images/studioLogo.webp";

class About extends Component {
    render() {
        return (
            <div>
                <h2>About Us</h2>
                <div className="pres-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="pres-description">
                        <Description>
                            <p>Deer Vision Studio is an <strong>independent</strong> studio, which develops video games. The studio is located in Belgium (Ardennes).</p>
                            <p>Our philosophy is to publish high quality games and to <strong>listen our community</strong> to constantly improve our games in the right direction. Therefore, it is always a real pleasure to discuss with you on our social networks (see links below).</p>
                            <p>We like to create our games without any restrictions. This is why we have a home-made game engine (<a className="text-link" href="https://github.com/petitg1987/urchinEngine" target="_blank" rel="noopener noreferrer" title="Git Hub">Urchin Engine</a>) and we produce most of our assets. This gives us a full control over the game development process and the only limitations are our imagination!</p>
                        </Description>
                    </div>
                    <div className="horizontal-spacer"/>
                    <div className="pres-logo">
                        <img src={studioLogo} alt="Studio Logo" width="300" height="300"/>
                    </div>
                </div>
            </div>
        );
    }
}

export default About;
