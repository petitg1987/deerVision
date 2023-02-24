import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionStartImg from "../../images/blogConsumptionStart.webp"

class Consumption extends Component {
    render() {
        return (
            <div>
                <h2>Electricity consumption of my game</h2>
                <div className="blog-container">
                    <div className="blog-content">
                        <p>Have you ever wondered how much electricity a video game consumes? This is the kind of question I ask myself out of curiosity.</p>
                        <img className={"blog-img-trans"} src={blogConsumptionStartImg} width={341} height={512} alt={"An electric pole"}/>

                        <div className={"blog-sub-title"}>Setup</div>
                        <p>My desktop computer:
                            <ul>
                                <li>CPU: Intel i7-8700K CPU @ 3.70GHz</li>
                                <li>Graphics card: NVidia 2080 Super</li>
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
                                <li>I simply plug my computer on a device measuring the electricity consumption</li>
                            </ul>
                        </p>

                    </div>
                </div>
            </div>
        );
    }
}

export default Consumption;
