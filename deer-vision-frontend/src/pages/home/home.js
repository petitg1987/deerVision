import React, { Component, Fragment } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.png";
import Description from "../../components/description/description";
import StudioPresentation from "../../components/studio-presentation/studio-presentation";
import SocialNetwork from "../../components/social-network/social-network";

class Home extends Component {
    render() {
        return (
            <div>
                <div id="news"/>
                <h2>News</h2>
                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

                <div id="games"/>
                <h2>Our Games</h2>
                <div className="pe-container">
                    <img src={photonEngineerLogo} alt="Twitter Logo" width="301px" height="75px"/>
                    <div className="pe-description">
                        <Description texts={
                            <Fragment>
                                <p>Photon Engineer is a <strong>puzzle/building game</strong>.</p>
                                <p>Your goal is to build an automated, efficient and optimized system which fit with the imposed constraints.</p>
                            </Fragment>
                        }/>
                    </div>
                </div>

                <div id="aboutUs"/>
                <h2>About Us</h2>
                <StudioPresentation/>
                <br/><br/><br/>
                <SocialNetwork/>
            </div>
        );
    }
}

export default Home;
