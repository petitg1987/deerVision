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
                    <div className="pres-description">
                        <p><img src={gregProfile} alt="Greg profile" width="140" height="174" className="pres-profile"/></p>
                        <Description>
                            <p>I am Grégory Petit, a <strong>solo game developer</strong> located in Belgium. Deer Vision Studio is the company I created to publish my games.</p>
                            <p>I am first and foremost a developer. For more than 10 years, I developed my own game engine (<a className="text-link" href="https://github.com/petitg1987/urchinEngine" target="_blank" rel="noopener noreferrer" title="Git Hub">Urchin Engine</a>) out of passion.</p>
                            <p>In 2021, I started to learn 3d modeling to create games using my game engine. Thus, I have a full control over the 3d models and programming which allows me to create games without restrictions and without dependencies to third parties.</p>
                        </Description>
                    </div>
                    <div className="pres-logo">
                        <img src={studioLogo} alt="Studio Logo" width="300" height="300"/>
                    </div>
                </div>
            </div>
        );
    }
}

export default About;
