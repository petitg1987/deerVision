import React, { Component } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.webp";
import Description from "../../components/description/description";
import SocialNetwork from "../../components/social-network/social-network";
import Button from "../../components/button/button";
import studioLogo from "../../images/studioLogo.webp";

class Home extends Component {
    render() {
        return (
            <div>
                {/*GAMES*/}
                <div id="games"/>
                <h2>Our Games</h2>
                <div className="game-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div>
                        <img src={photonEngineerLogo} className="pe-logo" alt="Photon Engineer Logo" width="300px" height="75px"/>
                    </div>
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="game-description">
                        <Description> {/*TODO: update game description*/}
                            <p>About the game:</p>
                            <ul>
                                <li>In year 2075, a space probe saw strange platforms floating in space near to Jupiter. The government decided to finance a space mission to send you on these platforms to elucidate the mystery.</li>
                                <li>Photon Engineer is a first-person <strong>puzzle game</strong>. Your goal is to build different types of blocks (mirror, pusher, merger...) efficiently to solve the challenges that await you.</li>
                             </ul>

                            <p>Join the community:</p>
                            <SocialNetwork logoSize={40} reeditGameUrl="https://www.reddit.com/r/PhotonEngineer/" onlyCommunityNetwork={true}/>

                            <p>Key request for Youtubers, Streamers and press (soon): {/*TODO: update description*/}</p>
                            <ul>
                                <li><a className="text-link" href="https://woovit.com/widget/offer/photon-engineer" target="_blank" rel="noopener noreferrer">Woovit</a></li> {/*TODO: update links*/}
                                <li><a className="text-link" href="https://www.keymailer.co/g/games/xyz1234" target="_blank" rel="noopener noreferrer">Keymailer</a></li> {/*TODO: update links*/}
                            </ul>
                        </Description>
                    </div>
                    <div className="horizontal-spacer"/>
                    <div className="game-btn-platform">
                        <Button text="TRAILER [SOON]" link="https://www.youtube.com/watch?v=XYZ1234"/> {/*TODO: update description/links*/}
                        <br/>
                        <Button text="BUY ON STEAM [SOON]" link="https://store.steampowered.com/app/1234/Photon_Engineer/"/> {/*TODO: update description/links*/}
                        <br/>
                        <Button text="BUY ON ITCH.IO [SOON]" link="https://deervision.itch.io/photon-engineer"/> {/*TODO: update description*/}
                    </div>
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="game-screenshots"> {/*TODO: update/add screenshots*/}
                        <div className="game-screenshot">
                            <img src="/photon-engineer/screenshot1_1440p.webp" alt="Photon Engineer screenshot 1" width="1100" height="619" />
                        </div>
                        <div className="game-screenshot">
                            <img src="/photon-engineer/screenshot2_1440p.webp" alt="Photon Engineer screenshot 2" width="1100" height="619" />
                        </div>
                        <div className="game-screenshot">
                            <img src="/photon-engineer/screenshot3_1440p.webp" alt="Photon Engineer screenshot 3" width="1100" height="619" />
                        </div>
                    </div>
                </div>
                {/*<hr/>*/}

                {/*ABOUT US*/}
                <div id="about-us"/>
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
                <br/><br/><br/>
                <div className="soc-container">
                    <SocialNetwork logoSize={50} label="Join us on:" onlyCommunityNetwork={false}/>
                </div>
            </div>
        );
    }
}

export default Home;
