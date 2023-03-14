import React, { Component } from 'react';
import "../pages.css"
import "./presskit.css"

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
                            <li><strong>Platform</strong>: PC (Windows / Linux)</li>
                            <li><strong>Steam page</strong>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"}>https://store.steampowered.com/app/2305110/Photon_Engineer/</a></li>
                            <li><strong>Creator / publisher</strong>: Grégory Petit</li>
                            <li><strong>Game Engine</strong>: <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"https://github.com/petitg1987/urchinEngine"}>Urchin Engine</a> (in-house engine)</li>
                            <li><strong>Development time</strong>: ~2 years (full time) on the game, ~10 years (spare time) on the game engine</li>
                        </ul>

                        <div className={"press-title"}>Assets</div>
                        <ul>
                            <li><strong>Screenshots</strong> (2560x1440): <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"/pe-presskit/screenshots.zip"}>screenshots.zip</a></li>
                            <li><strong>Logo</strong> (1482x400, transparent): <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"/pe-presskit/gameLogo.png"} download={true}>gameLogo.png</a></li>
                            <li><strong>Capsule</strong> (1280x720): <a className={"text-link"} target="_blank" rel="noopener noreferrer" href={"/pe-presskit/capsule16_9.png"} download={true}>capsule16_9.png</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default PePressKit;
