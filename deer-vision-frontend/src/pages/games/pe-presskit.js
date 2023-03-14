import React, { Component } from 'react';
import "../pages.css"
import "./presskit.css"
import gameLogo from '../../images/photon-engineer/presskit/gameLogo.png'
import gameLogoSimplified from '../../images/photon-engineer/presskit/gameLogoSimplified.png'
import capsuleSocNetwork from '../../images/photon-engineer/presskit/capsuleSocNetwork.png'
import capsuleSocNetworkText from '../../images/photon-engineer/presskit/capsuleSocNetworkText.png'

class PePressKit extends Component {
    render() {
        return (
            <div>
                <h2>Photon Engineer - Press kit</h2>
                <div className={"press-container"}>
                    <div className={"press-content"}>
                        <div>If you are a YouTuber, Twitch streamer, influencer or journalist, feel free to use our press kit for any content related to Photon Engineer.</div>
                        <div>To learn more about the Photon Engineer, please consult our <a className={"text-link"} href={"/"}>games</a> page.</div>
                        <div className={"press-title"}>Details</div>
                        <ul>
                            <li><strong>Name</strong>: Photon Engineer</li>
                            <li><strong>Platform</strong>: Windows 10/11, Linux (Ubuntu 22.04)</li>
                            <li><strong>Steam page</strong>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"}>https://store.steampowered.com/app/2305110/Photon_Engineer/</a></li>
                            <li><strong>Creator / publisher</strong>: Grégory Petit</li>
                            <li><strong>Game Engine</strong>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://github.com/petitg1987/urchinEngine"}>Urchin Engine</a> (in-house engine)</li>
                            <li><strong>Development time</strong>: ~2 years (full time) on the game, ~10 years (spare time) on the game engine</li>
                        </ul>

                        <div className={"press-title"}>Assets</div>
                        <ul>
                            <li>Game logo <small>(1482x400)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogo} download={true}>gameLogo.png</a></li>
                            <li>Game logo simplified <small>(1482x400)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogoSimplified} download={true}>gameLogoSimplified.png</a></li>
                            <li>Capsule <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetwork} download={true}>capsuleSocNetwork.png</a></li>
                            <li>Capsule with text <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetworkText} download={true}>capsuleSocNetworkText.png</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default PePressKit;
