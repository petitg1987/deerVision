import React, { Component } from 'react';
import "../pages.css"
import "./presskit.css"
import gameLogo from '../../images/photon-engineer/presskit/gameLogo.png'
import gameLogoSimplified from '../../images/photon-engineer/presskit/gameLogoSimplified.png'
import capsuleSocNetwork from '../../images/photon-engineer/presskit/capsuleSocNetwork.png'
import capsuleSocNetworkDesc from '../../images/photon-engineer/presskit/capsuleSocNetworkDesc.png'
import screenshot1 from '../../images/photon-engineer/screenshot1_1440p.webp'
import screenshot2 from '../../images/photon-engineer/screenshot2_1440p.webp'
import screenshot3 from '../../images/photon-engineer/screenshot3_1440p.webp'
import screenshot4 from '../../images/photon-engineer/screenshot4_1440p.webp'
import screenshot5 from '../../images/photon-engineer/screenshot5_1440p.webp'
import screenshot6 from '../../images/photon-engineer/screenshot6_1440p.webp'
import screenshot7 from '../../images/photon-engineer/screenshot7_1440p.webp'
import screenshot8 from '../../images/photon-engineer/screenshot8_1440p.webp'
import screenshot9 from '../../images/photon-engineer/screenshot9_1440p.webp'
import screenshot10 from '../../images/photon-engineer/screenshot10_1440p.webp'
import screenshot11 from '../../images/photon-engineer/screenshot11_1440p.webp'
import screenshot12 from '../../images/photon-engineer/screenshot12_1440p.webp'

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
                            <li>Capsule <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetwork}>capsuleSocNetwork.png</a></li>
                            <li>Capsule with description <small>(1280x720)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={capsuleSocNetworkDesc}>capsuleSocNetworkDesc.png</a></li>
                            <li>Logo <small>(1482x400)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogo}>gameLogo.png</a></li>
                            <li>Logo simplified <small>(1482x400)</small>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={gameLogoSimplified}>gameLogoSimplified.png</a></li>
                            <li>Screenshot <small>(2560x1440)</small>:&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot1}>s1</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot2}>s2</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot3}>s3</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot4}>s4</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot5}>s5</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot6}>s6</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot7}>s7</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot8}>s8</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot9}>s9</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot10}>s10</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot11}>s11</a>,&nbsp;
                                <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={screenshot12}>s12</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default PePressKit;
