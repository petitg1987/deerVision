import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogBalancingStartImg from "../../images/blog/blogBalancingStart.webp"
import blogBalancingSlidingPuzzleImg from "../../images/blog/blogBalancingSlidingPuzzle.gif"
import blogBalancingLaserPuzzleImg from "../../images/blog/blogBalancingLaserPuzzle.gif"
import blogConsumptionDeviceImg from "../../images/blog/blogConsumptionDevice.webp"
import blogConsumptionSettingsImg from "../../images/blog/blogConsumptionSettings.webp"
import blogConsumptionSceneImg from "../../images/blog/blogConsumptionScene.webp"

class BalancingPuzzles extends Component {

    render() {
        return (
            <div>
                <h2>Balancing the puzzles in my game</h2>
                <div className="blog-container">
                    <div className="blog-content">
                        <img className={"blog-img-trans"} src={blogBalancingStartImg} width={341} height={339} alt={"Rubiks cube"}/>
                        <p className={"center"}><big>Balancing puzzles in my game: what I've learned</big></p>
                        <p className={"center"}>My experience with balancing puzzles in my game may be limited, but I've learned a lot in the process and I am excited to share my insights with you.</p>

                        <div className={"blog-sub-title"}>Disclaimer</div>
                        <p>While I don't claim that the puzzles in my games (<a className={"text-link"} href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"} target={"_blank"} rel="noreferrer">Photon Engineer</a>) are flawlessly balanced, I have gained valuable experience by making mistakes, which I am eager to share with you. In addition, I receive player stats as my game is released, giving me a comprehensive view of the puzzle difficulty.</p>

                        <div className={"blog-sub-title"}>Game details</div>
                        <p>First of all, I'd like to provide some key details about my puzzle game for those who haven't played it.</p>
                        <p>The game features two distinct categories of puzzles, each of which presents a unique challenge to me in terms of balancing.</p>

                        <p><b>Laser puzzles:</b></p>
                        <p><img className={"blog-img"} src={blogBalancingLaserPuzzleImg} width={600} height={337} alt={"laser puzzle - Photon Engineer"}/></p>
                        <p>The player can build different blocks, each with unique features such as delaying a laser or pushing another block. The objective of the game is to redirect lasers into receptors and match a specific pattern.</p>

                        <p><b>Sliding puzzles:</b></p>
                        <p><img className={"blog-img"} src={blogBalancingSlidingPuzzleImg} width={600} height={337} alt={"sliding puzzle - Photon Engineer"}/></p>
                        <p>This is a 2D puzzle game where the objective is to use the red pieces to move the yellow piece to its target.</p>

                        <div className={"blog-sub-title"}>Balancing technique #1: self-evaluation</div>
                        <p>Self-evaluating puzzles as a solo game developer is clearly not an easy or effective task. However, depending on the puzzle type, it is still possible to make a rough evaluation of its difficulty.</p>
                        <p>Two techniques are available to make an approximate evaluation of the difficulty for <b>sliding puzzles</b>:</p>
                        <ul>
                            <li>Sliding puzzles require a certain number of moves to complete, which can be a useful but imperfect indicator of their difficulty. The number of pieces that can be moved also contributes to the difficulty of the puzzle: the more possibilities there are, the more complex the puzzle is likely to be.</li>
                            <li>It took me about two years to develop the game. As a result, I had the opportunity to partially forget the solutions to the early puzzles and experience them as if they were designed by someone else.</li>
                        </ul>
                        <p></p>
                        <p>However, both of these techniques do not work with laser puzzles. This is because this type of puzzle offers an infinite number of possibilities to place the blocks. Furthermore, I found that my brain remembered laser puzzles better than sliding puzzles, perhaps because they are 3D and took me longer to create.</p>

                        <div className={"blog-sub-title"}>Balancing technique #2: ask players</div>

                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p></p>
                        <p>Measurement device:</p>
                        <ul>
                            <li>I plug my computer on a device measuring the electricity consumption in Watt. Nothing fancy here.
                                <img className={"blog-img"} src={blogConsumptionDeviceImg} width={450} height={225} alt={"device to measure the electricity consumption"}/>
                            </li>
                            <li>Consumption of my computer idle: ~157 Watts</li>
                        </ul>

                        <div className={"blog-sub-title"}>Game settings</div>
                        <p>The game allows you to configure many settings that affect both performance and image quality. Here are the settings I used during my tests:</p>
                        <ul>
                            <li>Screen resolution: 4k, 2K, 1080p, etc.</li>
                            <li>FPS limit: limits the number of frames per second (FPS) from 40 to 200</li>
                            <li>Gamma: change the global brightness of the game</li>
                            <li>Graphics quality impacting mainly the shadow and lighting: low, medium, high</li>
                        </ul>
                        <p><img className={"blog-img"} src={blogConsumptionSettingsImg} width={600} height={383} alt={"configuration des paramètres du jeu"}/></p>

                        <p>Let's pick a scene from my puzzle game and start gathering data with different settings. Here is the 3D scene of the game I used to perform the measurements:</p>
                        <p><img className={"blog-img"} src={blogConsumptionSceneImg} width={600} height={383} alt={"game scene 1"}/></p>

                        <div className={"blog-sub-title"}>Results</div>
                        <p>I measured the electricity consumption of the game with a <strong>4K resolution</strong> in <strong>high quality</strong> and with different refresh rate: 40 FPS, 60 FPS, 90 FPS, 120 FPS, 140 FPS and 200 FPS. Here are the results:</p>
                        <div className={"graph-container"}><canvas id="resultScene1"/></div>
                        <p></p>
                        <p>I didn't really know what to expect but one thing surprised me at lot: the game consumes almost 9 times more electricity at 200 FPS compared to 40 FPS.</p>

                        <p></p>
                        <p>Let's try another set of measurements with the following game settings: <strong>2K resolution</strong> in <strong>medium quality</strong>:</p>
                        <div className={"graph-container"}><canvas id="resultScene1v2"/></div>
                        <p></p>
                        These results blew me away. The difference in image quality between both configurations is not easy to notice, but the difference in electricity consumption is quite significant.
                        <p><small><u>Disclaimer</u>: these results are clearly specific to my configuration and to the game. I guess a lot of factors can produce very different results: laptop vs. desktop computer, game with CPU vs. GPU bottlenecks, etc.</small></p>

                        <div className={"blog-sub-title"}>More experiments</div>
                        <p>I try more measurements but without interesting results:</p>
                        <ul>
                            <li>Brightness/gamma settings: insignificant change in consumption</li>
                            <li>Game in 2K fullscreen and 2K windowed: insignificant change in consumption</li>
                            <li>I try to stress more the CPU with game physics: minor change in consumption</li>
                            <li>Use different scenes from the game: similar results compared to those above</li>
                        </ul>
                        <p></p>
                        <p>It seems that only the amount of work provided to the GPU significantly affects the overall power consumption.</p>
                        <p>I also measured other topics related to gaming:</p>
                        <ul>
                            <li>Watch a stream on Twitch: <strong>~3 watts</strong></li>
                            <li>Record a video with OBS: <strong>~42 watts</strong></li>
                        </ul>
                        <p></p>
                    </div>
                </div>
            </div>
        );
    }
}

export default BalancingPuzzles;
