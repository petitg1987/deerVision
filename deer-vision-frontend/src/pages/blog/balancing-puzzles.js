import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogBalancingStartImg from "../../images/blog/blogBalancingStart.webp"
import blogBalancingSlidingPuzzleImg from "../../images/blog/blogBalancingSlidingPuzzle.gif"
import blogBalancingLaserPuzzleImg from "../../images/blog/blogBalancingLaserPuzzle.gif"
import blogBalancingStats1Img from "../../images/blog/blogBalancingStats1.webp"
import blogBalancingStats2Img from "../../images/blog/blogBalancingStats2.webp"

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

                        <div className={"blog-sub-title"}>Balancing technique #2: find players</div>
                        <p>One obvious way to evaluate the difficulty of puzzles is to have people play the game and provide feedback on each puzzle. This technique is probably one of the best, but it can be difficult to implement. Since this is my first game, the players don't know me and most of them are not interested in playing a game in development. Additionally, it may take some time for them to provide feedback.</p>
                        <p>I found some people but I had the feeling that it was not sufficient to have a good balancing in all my puzzles.</p>

                        <div className={"blog-sub-title"}>Balancing technique #3: gather statistics</div>
                        <p>Instead of requesting feedback from players about the difficulty of a puzzle, I came up with a new idea: add timing statistics. I implemented a time measurement feature to track the time taken to solve each puzzle, and then sent the statistics to a server. Finally, I aggregated the results to gain a clearer understanding of the difficulties of each puzzle.</p>
                        <p>Here is the result for one puzzle:</p>
                        <p><img className={"blog-img"} src={blogBalancingStats1Img} width={600} height={349} alt={"Puzzle statistics 1 - Photon Engineer"}/></p>
                        <p>This graph shows that 1 player found the solution to the puzzle in 3 min, 3 players found the solution in 4 minutes, etc.</p>

                        <p>Here is another graph:</p>
                        <p><img className={"blog-img"} src={blogBalancingStats2Img} width={600} height={349} alt={"Puzzle statistics 2 - Photon Engineer"}/></p>
                        <p>This graph shows a significant difference in the resolution time of the puzzle in level 20. It appears that some players can find the solution in about 9 minutes while others take more than 30 minutes. This result is concerning and indicates that I should review this particular puzzle.</p>

                        <p>I was quite pleased with these statistics and believed that it would be the perfect solution for balancing puzzles in my game. However, I later discovered that I was mistaken...</p>

                        <div className={"blog-sub-title"}>Balancing technique #4: game recording</div>

                        <p></p>
                    </div>
                </div>
            </div>
        );
    }
}

export default BalancingPuzzles;
