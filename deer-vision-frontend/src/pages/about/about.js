import React, { Component } from 'react';
import "../pages.css"
import "./about.css"
import Description from "../../components/description/description";
import studioLogo from "../../images/studioLogo.webp";
import gregProfile from "../../images/gregProfile.webp";

class About extends Component {
    render() {
        return (
            <div>
                <h2>About</h2>
                <div className="pres-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="pres-description">
                        <Description>
                            <p><img src={gregProfile} alt="Greg profile" width="150" height="186" className="pres-profile"/></p>
                            <p>My name is Grégory Petit and I'm a <strong>solo game developer</strong> located in Belgium. Deer Vision Studio is the company name used to publish and sell the games.</p>
                            <p>My philosophy is to publish high quality games and <strong>listen the community</strong> to constantly improve them in the right direction.</p>
                            <p>I like to create my games without any restrictions. That is why I developed my own game engine (<a className="text-link" href="https://github.com/petitg1987/urchinEngine" target="_blank" rel="noopener noreferrer" title="Git Hub">Urchin Engine</a>) and I produce most of the game assets. This gives me a full control over the game development process and the only limit is my imagination!</p>
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
