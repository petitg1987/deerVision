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

    async componentDidMount() {
        let screenshotsModal = document.getElementById('screenshots-modal');
        let body = document.getElementsByTagName("body")[0]
        let gameImages = document.getElementsByClassName('game-screenshot-img');
        for (let gameImage of gameImages) {
            gameImage.onclick = function(event) {
                if (!window.matchMedia("(max-width: 800px)").matches) {
                    let modalImg = document.getElementById("screenshots-modal-content-img");
                    let zoomInImage = this.src.replace("_720p", "_1440p");
                    modalImg.src = ""; //for slow connection: display white/empty image first instead of the previous one displayed
                    modalImg.src = zoomInImage;

                    body.style.cursor = "pointer";
                    screenshotsModal.style.display = "flex";
                    event.stopPropagation();
                }
            }
        }

        document.body.onclick = function() {
            if (screenshotsModal.style.display === "flex") {
                body.style.cursor = "auto";
                screenshotsModal.style.display = "none";
            }
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
                        <div className="game-description">
                            <Description> {/*TODO: update game description*/}
                                <p>About the game:</p>
                                <ul className="game-about">
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
                            <img className="game-screenshot-img" src="/photon-engineer/screenshot1_720p.webp" alt="Photon Engineer screenshot 1" width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src="/photon-engineer/screenshot2_720p.webp" alt="Photon Engineer screenshot 2" width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src="/photon-engineer/screenshot3_720p.webp" alt="Photon Engineer screenshot 3" width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src="/photon-engineer/screenshot4_720p.webp" alt="Photon Engineer screenshot 4" width="1280" height="720" />
                        </div>
                        <div className="game-screenshot">
                            <img className="game-screenshot-img" src="/photon-engineer/screenshot5_720p.webp" alt="Photon Engineer screenshot 5" width="1280" height="720" />
                        </div>

                        <div id="screenshots-modal" className="screenshots-modal">
                            <img className="screenshots-modal-content" id="screenshots-modal-content-img" alt="Zoom-in screenshot" />
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}

export default Home;
