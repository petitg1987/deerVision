import React, { Component } from 'react';
import SocialNetwork from "../../components/social-network/social-network";
import Presentation from "../../components/presentation/presentation";

class AboutUs extends Component {
    render() {
        return (
            <div>
                <h2>About Us</h2>
                <Presentation/>
                <br/><br/><br/>
                <SocialNetwork/>
            </div>
        );
    }
}

export default AboutUs;