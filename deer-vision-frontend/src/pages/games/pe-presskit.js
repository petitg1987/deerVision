import React, { Component } from 'react';
import "../pages.css"
import "./presskit.css"
import gameLogo from '../../images/photon-engineer/presskit/gameLogo.png'
import gameLogoSimplified from '../../images/photon-engineer/presskit/gameLogoSimplified.png'
import capsuleSocNetwork from '../../images/photon-engineer/presskit/capsuleSocNetwork.png'
import capsuleSocNetworkDesc from '../../images/photon-engineer/presskit/capsuleSocNetworkDesc.png'
import screenshot1 from '../../images/photon-engineer/screenshot1.png'
import screenshot2 from '../../images/photon-engineer/screenshot2.png'
import screenshot3 from '../../images/photon-engineer/screenshot3.png'
import screenshot4 from '../../images/photon-engineer/screenshot4.png'
import screenshot5 from '../../images/photon-engineer/screenshot5.png'
import screenshot6 from '../../images/photon-engineer/screenshot6.png'
import screenshot7 from '../../images/photon-engineer/screenshot7.png'
import screenshot8 from '../../images/photon-engineer/screenshot8.png'
import screenshot9 from '../../images/photon-engineer/screenshot9.png'
import screenshot10 from '../../images/photon-engineer/screenshot10.png'
import screenshot11 from '../../images/photon-engineer/screenshot11.png'
import screenshot12 from '../../images/photon-engineer/screenshot12.png'

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
                            <li><i>Name</i>: Photon Engineer</li>
                            <li><i>Platform</i>: Windows 10/11, Linux (Ubuntu 22.04)</li>
                            <li><i>Steam page</i>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"}>https://store.steampowered.com/app/2305110/Photon_Engineer/</a></li>
                            <li><i>Creator / publisher</i>: Grégory Petit</li>
                            <li><i>Game Engine</i>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://github.com/petitg1987/urchinEngine"}>Urchin Engine</a> (in-house engine)</li>
                            <li><i>Development time</i>: ~2 years (full time) on the game, ~10 years (spare time) on the game engine</li>
                        </ul>

                        <div className={"press-title"}>Assets</div>
                        <ul>
                            <li>Capsule <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetwork}>capsuleSocNetwork.png</a></li>
                            <li>Capsule with description <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetworkDesc}>capsuleSocNetworkDesc.png</a></li>
                            <li>Logo <small>(1482x400, transparent)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogo}>gameLogo.png</a></li>
                            <li>Logo simplified <small>(1482x400, transparent)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogoSimplified}>gameLogoSimplified.png</a></li>
                            <li>Screenshots <small>(2560x1440)</small>:
                                <ul>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot1}>screenshot1.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot2}>screenshot2.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot3}>screenshot3.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot4}>screenshot4.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot5}>screenshot5.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot6}>screenshot6.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot7}>screenshot7.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot8}>screenshot8.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot9}>screenshot9.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot10}>screenshot10.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot11}>screenshot11.png</a></li>
                                    <li><a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot12}>screenshot12.png</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default PePressKit;
