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

class Home extends Component {

    constructor(props) {
        super(props);
        this.onTrailerClick = this.onTrailerClick.bind(this);
    }

    async componentDidMount() {
        document.body.onclick = function() {
            let trailerModal = document.getElementById('trailer-modal');
            if (trailerModal && trailerModal.style.display === "flex") {
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
        modalIframe.src = "https://www.youtube.com/embed/ADOYXmTux5g";

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
                        <img src={photonEngineerLogo} className="pe-logo" alt="Photon Engineer Logo" width="320px" height="150px"/>
                    </div>
                    <div className="game-info-container">

                        <Description>
                            <p>Gameplay</p>
                            <div className="game-description">
                                <div>During your journey, you will be confronted with a variety of original and challenging puzzles. You will be required to form ingenious systems by building blocks having different abilities, such as deflecting and merging laser beams, turning and pushing other blocks.</div><br/>
                                <div>Unleash your creativity and problem-solving skills to progress through the game, unravel the mysteries of your surroundings and unlock new information as you delve deeper into the unknown.</div><br/>
                                <div>The laser puzzles are interspersed with sliding puzzles that require strategic moves to solve.</div>
                            </div>

                            <p>Story</p>
                            <div className="game-description">
                                <div>In 2075, a space probe saw a strange platform floating in space near Jupiter. The government has decided to finance a space mission to send you on this platform to elucidate the mystery.</div><br/>
                                <div>Your mission is to discover why this platform exists and who built it.</div>
                            </div>
                        </Description>

                        <div className="game-btn-platform">
                            <br/>
                            <Button text="WATCH TRAILER" link="" clickEvent={this.onTrailerClick} squareLogo={youtubeSquareLogo}/>
                            <br/>
                            <Button text="STEAM PAGE" link="https://store.steampowered.com/app/2305110?utm_source=website-main" squareLogo={steamSquareLogo}/>
                        </div>
                        <div id="trailer-modal" className="content-modal">
                            <iframe id="trailer-modal-content" className="trailer-modal-content" width="560" height="315" src=""
                                    title="Photon Engineer Trailer"
                                    allow="accelerometer; gyroscope"
                                    allowFullScreen></iframe>
                        </div>
                    </div>

                    <Description>
                        <p>Join the community</p>
                        <div className="game-soc-container">
                            <SocialNetwork logoSize={40}/>
                        </div>
                    </Description>

                    <Description>
                        <p>Screenshots</p>
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

                        <div id="screenshots-modal" className="content-modal">
                            <img id="screenshots-modal-content" className="screenshots-modal-content" alt="Zoom-in screenshot" />
                        </div>
                    </div>

                    <Description>
                        <p>Further details</p>
                        <div className="game-description">
                            <ul>
                                <li>40 levels/platforms to discover.</li>
                                <li>95 puzzles (45 puzzles with lasers and 50 sliding puzzles).</li>
                                <li>15 puzzle achievements that extend the game experience.</li>
                                <li>Each puzzle is unique and provides a new idea.</li>
                                <li>The further you go, the more complex mechanisms you'll encounter.</li>
                                <li>Lasers are all marked with symbols for colorblind players.</li>
                            </ul>
                        </div>
                    </Description>
                    <br/>

                    <Description>
                        <p>Influencers & Press</p>
                        <div className="game-description">
                            <ul>
                                <li>Steam keys for influencers and press: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href="https://www.keymailer.co/g/games/169095">Keymailer</a> / <a className={"text-link"} target="_blank" rel="noopener noreferrer" href="https://woovit.com/offer/photon-engineer/">Woovit</a>.</li>
                                <li>Feel free to use our <a className={"text-link"} href="/games/pe-press-kit">press kit</a> as you wish.</li>
                            </ul>
                        </div>
                    </Description>
                    <br/>
                </div>
            </div>
        );
    }
}

export default Home;
