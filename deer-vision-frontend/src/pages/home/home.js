import React, { Component } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.png";
import Description from "../../components/description/description";
import SocialNetwork from "../../components/social-network/social-network";
import "react-responsive-carousel/lib/styles/carousel.min.css"; // requires a loader
import { Carousel } from 'react-responsive-carousel';
import Button from "../../components/button/button";
import studioLogo from "../../images/studioLogo.png";

class Home extends Component {
    render() {
        return (
            <div>
                {/*NEWS*/}
                <div id="news"/>
                <h2>News</h2>
                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

                {/*GAMES*/}
                <hr/>
                <div id="games"/>
                <h2>Our Games</h2>
                <div className="pe-container">
                    <div className="home-vertical-spacer"/>
                    <div className="home-horizontal-spacer"/>
                    <div className="pe-title">
                        <img src={photonEngineerLogo} alt="Photon Engineer Logo" width="300px" height="75px"/>
                    </div>
                    <div className="home-vertical-spacer"/>
                    <div className="home-horizontal-spacer"/>
                    <div className="pe-description">
                        <Description>
                            <p>Photon Engineer is a <strong>puzzle/building game</strong>. Your goal is to build an automated, efficient and optimized system which fit with the imposed constraints.</p>
                            <p>Community links: <SocialNetwork logoSize={30} reeditGameUrl="https://www.reddit.com/r/PhotonEngineer/" onlyCommunityNetwork={true}/></p>
                            <p>Key request for Youtubers, Streamers and press:
                                <a href="https://woovit.com/widget/offer/photon-engineer" target="_blank" rel="noopener noreferrer">Woovit</a>, {/*TODO: update links*/}
                                <a href="https://www.keymailer.co/g/games/xyz1234" target="_blank" rel="noopener noreferrer">Keymailer</a></p> {/*TODO: update links*/}
                        </Description>
                    </div>
                    <div className="home-horizontal-spacer"/>
                    <div className="pe-btn-platform">
                        <Button text="WATCH TRAILER" link="https://www.youtube.com/watch?v=XYZ1234"/> {/*TODO: update links*/}
                        <br/>
                        <Button text="AVAILABLE ON STEAM" link="https://store.steampowered.com/app/1234/Photon_Engineer/"/> {/*TODO: update links*/}
                        <br/>
                        <Button text="AVAILABLE ON ITCH.IO" link="https://deep-vision.itch.io/photon-engineer"/> {/*TODO: update links*/}
                    </div>
                    <div className="home-vertical-spacer"/>
                    <div className="pe-screenshots">
                        <Carousel autoPlay={false} showThumbs={false} infiniteLoop={false} showStatus={false}>
                                <img src="/photon-engineer/screenshot1.png" alt="Photon Engineer screenshot 1" />
                                <img src="/photon-engineer/screenshot2.png" alt="Photon Engineer screenshot 2" />
                                <img src="/photon-engineer/screenshot3.png" alt="Photon Engineer screenshot 3" />
                        </Carousel>
                    </div>
                </div>

                {/*ABOUT US*/}
                <hr/>
                <div id="aboutUs"/>
                <h2>About Us</h2>
                <div className="pres-container">
                    <div className="home-vertical-spacer"/>
                    <div className="home-horizontal-spacer"/>
                    <div className="pres-description">
                        <Description>
                            <p>Deer Vision Studio is an <strong>independent</strong> studio, which develops video games. The studio is located in Belgium (Ardennes).</p>
                            <p>Our philosophy is to publish high quality games and listen our community to constantly improve our games in the right direction. Therefore, it is always a real pleasure to discuss with you on our social networks (see links below).</p>
                            <p>We like to create our games without any technical restrictions. This is why we have a home-made game engine named "Urchin Engine" that we have been developing for more than 10 years. The engine is free and open source: <a href="https://github.com/petitg1987/urchinEngine" target="_blank" rel="noopener noreferrer">GitHub</a></p>
                        </Description>
                    </div>
                    <div className="home-horizontal-spacer"/>
                    <div className="pres-logo">
                        <img src={studioLogo} alt="Studio Logo" width="300" height="300"/>
                    </div>
                </div>
                <br/><br/><br/>
                <div className="soc-container">
                    <SocialNetwork logoSize={50} label="Join us on:" onlyCommunityNetwork={false}/>
                </div>
            </div>
        );
    }
}

export default Home;
