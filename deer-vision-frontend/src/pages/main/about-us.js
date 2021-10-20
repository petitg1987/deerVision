import React, { Component } from 'react';
import SocialNetwork from "../../components/social-network/social-network";
import StudioPresentation from "../../components/studio-presentation/studio-presentation";

class AboutUs extends Component {
    render() {
        return (
            <div>
                <h2>About Us</h2>
                <StudioPresentation/>
                <br/><br/><br/>
                <SocialNetwork/>
            </div>
        );
    }
}

export default AboutUs;