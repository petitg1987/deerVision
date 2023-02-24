import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionStartImg from "../../images/blogConsumptionStart.webp"
import blogConsumptionDeviceImg from "../../images/blogConsumptionDevice.webp"
import blogConsumptionSettingsImg from "../../images/blogConsumptionSettings.webp"
import blogConsumptionScene1Img from "../../images/blogConsumptionScene1.webp"

class Consumption extends Component {
    render() {
        return (
            <div>
                <h2>Electricity consumption of my game</h2>
                <div className="blog-container">
                    <div className="blog-content">
                        <img className={"blog-img-trans"} src={blogConsumptionStartImg} width={341} height={512} alt={"An electric pole"}/>
                        <p className={"center"}>Have you ever wondered how much electricity a video game consumes? My brain asks this kind of questions and needs an answer.</p>

                        <div className={"blog-sub-title"}>Setup</div>
                        <p>Let's start by checking the setup I used to measure the electricity consumption of my puzzle game.</p>
                        <p>My desktop computer:
                            <ul>
                                <li>CPU: Intel i7-8700K CPU @ 3.70GHz</li>
                                <li>Graphics card: Nvidia 2080 Super</li>
                                <li>RAM: 32Go</li>
                                <li>Disk: 980 PRO PCle 4.0 NVMe M.2 SSD</li>
                                <li>Two screens - 4K</li>
                            </ul>
                        </p>

                        <p>The game (<a className={"text-link"} href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"} target={"_blank"} rel="noreferrer">Photon Engineer</a>):
                            <ul>
                                <li>3D environment but with limited number of models</li>
                                <li>Bottleneck on the GPU (CPU usage: ~20%)</li>
                            </ul>
                        </p>

                        <p>Measurement device:
                            <ul>
                                <li>I plug my computer on a device measuring the electricity consumption in Watt. Nothing fancy here.
                                    <img className={"blog-img"} src={blogConsumptionDeviceImg} width={450} height={225} alt={"device to measure the electricity consumption"}/>
                                </li>
                                <li>Consumption of my computer (idle): ~157 Watt</li>
                            </ul>
                        </p>

                        <div className={"blog-sub-title"}>Configuration</div>
                        <p>The game allows to configure a lot of settings directly impacting the performance. Here are the used one during my tests:
                        <ul>
                            <li>Screen resolutions: 4k, 2K, 1080p, etc.</li>
                            <li>FPS limit: limit the number of frames per second (FPS) from 40 to 200 or unlimited</li>
                            <li>Gamma: change the global lighting of the game</li>
                            <li>Graphics quality impacting shadow, lighting, etc.</li>
                        </ul></p>
                        <p><img className={"blog-img"} src={blogConsumptionSettingsImg} width={600} height={383} alt={"configuration des paramètres du jeu"}/></p>

                        <div className={"blog-sub-title"}>Results (scene 1)</div>
                        <p>Let's pickup a scene of my puzzle game and let's start to gather some data with different settings.</p>
                        <p>The scene used to perform the measurement: <img className={"blog-img"} src={blogConsumptionScene1Img} width={600} height={383} alt={"game scene 1"}/></p>
                        <p>I measured the electricity consumption of the game in 4K with high setting and with different refresh rate: 40 FPS, 60 FPS, 90FPS, 140FPS and unlimited. Here is the result: </p>

                    </div>
                </div>
            </div>
        );
    }
}

export default Consumption;
