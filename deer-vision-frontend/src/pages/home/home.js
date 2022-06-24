import React, { Component } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.webp";
import Description from "../../components/description/description";
import SocialNetwork from "../../components/social-network/social-network";
import Button from "../../components/button/button";
import youtubeSquareLogo from "../../images/youtubeSquareLogo.webp";
import steamSquareLogo from "../../images/steamSquareLogo.webp";
import itchSquareLogo from "../../images/itchSquareLogo.webp";

class Home extends Component {
    render() {
        return (
            <div>
                <h2>Games</h2>
                <div className="game-container">
                    <div>
                        <img src={photonEngineerLogo} className="pe-logo" alt="Photon Engineer Logo" width="300px" height="75px"/>
                    </div>
                    <div className="game-info-container">
                        <div className="game-description">
                            <Description> {/*TODO: update game description*/}
                                <p>About the game:</p>
                                <ul>
                                    <li>In the year 2075, a space probe saw a strange platform floating in space close to Jupiter. The government decided to finance a space mission to send you on this platform to elucidate the mystery.</li>
                                    <li>Photon Engineer is a first-person <strong>puzzle game</strong>. Your goal is to build different types of blocks (mirror, pusher, merger...) efficiently to solve the challenges that await you.</li>
                                 </ul>

                                <p>Join the community:</p>
                                <div className="game-soc-container">
                                    <SocialNetwork logoSize={40}/>
                                </div>

                                <p>Key request for Youtubers, Streamers and press:</p>
                                <ul>
                                    <li><a className="text-link" href="https://woovit.com/widget/offer/photon-engineer" target="_blank" rel="noopener noreferrer">Woovit</a> (soon)</li> {/*TODO: update description/links*/}
                                    <li><a className="text-link" href="https://www.keymailer.co/g/games/xyz1234" target="_blank" rel="noopener noreferrer">Keymailer</a> (soon)</li> {/*TODO: update description/links*/}
                                </ul>
                            </Description>
                        </div>
                        <div className="game-btn-platform">
                            <br/>
                            <Button text="TRAILER [SOON]" link="https://www.youtube.com/watch?v=XYZ1234" squareLogo={youtubeSquareLogo}/> {/*TODO: update description/links*/}
                            <br/>
                            <Button text="BUY ON STEAM [SOON]" link="https://store.steampowered.com/app/1234/Photon_Engineer/" squareLogo={steamSquareLogo}/> {/*TODO: update description/links*/}
                            <br/>
                            <Button text="BUY ON ITCH.IO [SOON]" link="https://deervision.itch.io/photon-engineer" squareLogo={itchSquareLogo}/> {/*TODO: update description*/}
                        </div>
                    </div>
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
            </div>
        );
    }
}

export default Home;
