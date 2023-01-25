import React, { Component } from 'react';
import "../pages.css"
import "./home.css"
import photonEngineerLogo from "../../images/photon-engineer/logo.webp";
import Description from "../../components/description/description";
import SocialNetwork from "../../components/social-network/social-network";
import Button from "../../components/button/button";
import youtubeSquareLogo from "../../images/youtubeSquareLogo.webp";
import steamSquareLogo from "../../images/steamSquareLogo.webp";
import screenshot1 from "../../images/photon-engineer/screenshot1_720p.webp";
import screenshot2 from "../../images/photon-engineer/screenshot2_720p.webp";
import screenshot3 from "../../images/photon-engineer/screenshot3_720p.webp";
import screenshot4 from "../../images/photon-engineer/screenshot4_720p.webp";
import screenshot5 from "../../images/photon-engineer/screenshot5_720p.webp";
import screenshot6 from "../../images/photon-engineer/screenshot6_720p.webp";
import screenshot7 from "../../images/photon-engineer/screenshot7_720p.webp";
import screenshot8 from "../../images/photon-engineer/screenshot8_720p.webp";
import screenshot9 from "../../images/photon-engineer/screenshot9_720p.webp";
import screenshot10 from "../../images/photon-engineer/screenshot10_720p.webp";
import screenshot11 from "../../images/photon-engineer/screenshot11_720p.webp";
import screenshot12 from "../../images/photon-engineer/screenshot12_720p.webp";

class Home extends Component {

    constructor(props) {
        super(props);
        this.onTrailerClick = this.onTrailerClick.bind(this);
    }

    async componentDidMount() {
        document.body.onclick = function() {
            let trailerModal = document.getElementById('trailer-modal');
            if (trailerModal.style.display === "flex") {
                let modalIframe = document.getElementById("trailer-modal-content");
                modalIframe.src = "";
                let body = document.getElementsByTagName("body")[0];
                body.style.cursor = "auto";
                trailerModal.style.display = "none";
            }

            let screenshotsModal = document.getElementById('screenshots-modal');
            if (screenshotsModal.style.display === "flex") {
                let body = document.getElementsByTagName("body")[0];
                body.style.cursor = "auto";
                screenshotsModal.style.display = "none";
            }
        }
    }

    onTrailerClick(event) {
        let trailerModal = document.getElementById('trailer-modal');
        let modalIframe = document.getElementById("trailer-modal-content");
        modalIframe.src = "https://www.youtube.com/embed/ddy12WHqt-M"; /*TODO: update links*/

        let body = document.getElementsByTagName("body")[0];
        body.style.cursor = "pointer";
        trailerModal.style.display = "flex";
        event.stopPropagation();
    }

    onScreenshotClick(event, screenshotId) {
        let screenshotsModal = document.getElementById('screenshots-modal');
        if (!window.matchMedia("(max-width: 800px)").matches) {
            let modalImg = document.getElementById("screenshots-modal-content");
            modalImg.src = ""; //for slow connection: display white/empty image first instead of the previous one displayed
            modalImg.src = require('../../images/photon-engineer/screenshot' + screenshotId + '_1440p.webp');

            let body = document.getElementsByTagName("body")[0];
            body.style.cursor = "pointer";
            screenshotsModal.style.display = "flex";
            event.stopPropagation();
        }
    }

    render() {
        return (
            <div>
                <h2>Games</h2>
                <div className="game-container">
                    <div>
                        <img src={photonEngineerLogo} className="pe-logo" alt="Photon Engineer Logo" width="300px" height="75px"/>
                    </div>
                    <div className="game-info-container">

                        <Description>
                            <p>About the game:</p>
                            <div className="game-description">
                                In the year 2075, a space probe saw a strange platform floating in space close to Jupiter. The government has decided to finance a space mission to send you on this platform to elucidate the mystery.
                                <br/><br/>During your journey, you will face a variety of original and challenging puzzles:
                                <ul className="game-about">
                                    <li>You will need to redirect and adjust the laser beams by using the environment or by building blocks. These blocks have the capability to interact with laser beams to change their color, direction, deactivate them and much more.</li>
                                    <li>There are also 2d puzzles where you have to move pieces in a certain order to reach a target. As you progress, these puzzles will evolve with more rules to gradually increase the difficulty.</li>
                                </ul>
                                Altogether, more than <strong>90 puzzles</strong> await you for several hours of play.
                            </div>
                        </Description>

                        <div className="game-btn-platform">
                            <br/>
                            <Button text="TRAILER [SOON]" link="" clickEvent={this.onTrailerClick} squareLogo={youtubeSquareLogo}/> {/*TODO: update description*/}
                            <br/>
                            <Button text="BUY ON STEAM [SOON]" link="https://store.steampowered.com/app/1234/Photon_Engineer/" squareLogo={steamSquareLogo}/> {/*TODO: update description/links*/}
                        </div>
                        <div id="trailer-modal" className="content-modal">
                            <iframe id="trailer-modal-content" className="trailer-modal-content" width="560" height="315" src=""
                                    title="Photon Engineer Trailer"
                                    allow="accelerometer; gyroscope"
                                    allowFullScreen></iframe>
                        </div>
                    </div>

                    <Description>
                        <p>Join the community:</p>
                        <div className="game-soc-container">
                            <SocialNetwork logoSize={40}/>
                        </div>
                    </Description>

                    <Description>
                        <p>Screenshots:</p>
                    </Description>
                    <div className="game-screenshots">
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot1} alt="Photon Engineer screenshot 1" onClick={(event) => this.onScreenshotClick(event, 1)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot2} alt="Photon Engineer screenshot 2" onClick={(event) => this.onScreenshotClick(event, 2)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot3} alt="Photon Engineer screenshot 3" onClick={(event) => this.onScreenshotClick(event, 3)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot4} alt="Photon Engineer screenshot 4" onClick={(event) => this.onScreenshotClick(event, 4)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot5} alt="Photon Engineer screenshot 5" onClick={(event) => this.onScreenshotClick(event, 5)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot6} alt="Photon Engineer screenshot 6" onClick={(event) => this.onScreenshotClick(event, 6)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot7} alt="Photon Engineer screenshot 7" onClick={(event) => this.onScreenshotClick(event, 7)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot8} alt="Photon Engineer screenshot 8" onClick={(event) => this.onScreenshotClick(event, 8)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot9} alt="Photon Engineer screenshot 9" onClick={(event) => this.onScreenshotClick(event, 9)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot10} alt="Photon Engineer screenshot 10" onClick={(event) => this.onScreenshotClick(event, 10)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot11} alt="Photon Engineer screenshot 11" onClick={(event) => this.onScreenshotClick(event, 11)} width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src={screenshot12} alt="Photon Engineer screenshot 12" onClick={(event) => this.onScreenshotClick(event, 12)} width="1280" height="720" />
                        </div>

                        <div id="screenshots-modal" className="content-modal">
                            <img id="screenshots-modal-content" className="screenshots-modal-content" alt="Zoom-in screenshot" />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;
