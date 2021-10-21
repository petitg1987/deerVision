import React, { Component } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.png";
import Description from "../../components/description/description";
import StudioPresentation from "../../components/studio-presentation/studio-presentation";
import SocialNetwork from "../../components/social-network/social-network";
import "react-responsive-carousel/lib/styles/carousel.min.css"; // requires a loader
import { Carousel } from 'react-responsive-carousel';
import Button from "../../components/button/button";

class Home extends Component {
    render() {
        return (
            <div>
                <div id="news"/>
                <h2>News</h2>
                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

                <hr/>
                <div id="games"/>
                <h2>Our Games</h2>
                <div className="pe-container">
                    <div className="pe-title">
                        <img src={photonEngineerLogo} alt="Twitter Logo" width="300px" height="75px"/>
                    </div>
                    <div className="pe-description">
                        <Description>
                            <p>Photon Engineer is a <strong>puzzle/building game</strong>.</p>
                            <p>Your goal is to build an automated, efficient and optimized system which fit with the imposed constraints.</p>
                        </Description>
                    </div>
                    <div className="pe-btn-platform">
                        <Button text="Available on Steam"/>
                        <br/>
                        <Button text="Available on Itch.io"/>
                    </div>
                    <div className="pe-screenshots">
                        <Carousel autoPlay={true} interval={3000} showThumbs={false} infiniteLoop={true} showStatus={false}>
                            <div>
                                <img src="/photon-engineer/screenshot1.png" alt="Photon Engineer screenshot 1" />
                            </div>
                            <div>
                                <img src="/photon-engineer/screenshot2.png" alt="Photon Engineer screenshot 2" />
                            </div>
                            <div>
                                <img src="/photon-engineer/screenshot3.png" alt="Photon Engineer screenshot 3" />
                            </div>
                        </Carousel>
                    </div>
                </div>

                <hr/>
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
