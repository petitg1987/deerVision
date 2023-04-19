import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogPuzzlesComplexityStartImg from "../../images/blog/blogPuzzlesComplexityStart.webp"
import blogPuzzlesComplexitySlidingPuzzleImg from "../../images/blog/blogPuzzlesComplexitySlidingPuzzle.gif"
import blogPuzzlesComplexityLaserPuzzleImg from "../../images/blog/blogPuzzlesComplexityLaserPuzzle.gif"
import blogPuzzlesComplexityStats1Img from "../../images/blog/blogPuzzlesComplexityStats1.webp"
import blogPuzzlesComplexityStats2Img from "../../images/blog/blogPuzzlesComplexityStats2.webp"
import blogPuzzlesComplexityBlockRotationImg from "../../images/blog/blogPuzzlesComplexityBlockRotation.gif"

class PuzzlesComplexity extends Component {

    render() {
        return (
            <div>
                <h2>Puzzles complexity assessment</h2>
                <div className="blog-container">
                    <div className="blog-content">
                        <img className={"blog-img-trans"} src={blogPuzzlesComplexityStartImg} width={341} height={339} alt={"Rubiks cube"}/>
                        <p className={"center"}><big>How I evaluated the complexity of the puzzles in my game</big></p>

                        <div className={"blog-sub-title"}>Disclaimer</div>
                        <p>While I don't claim that the puzzles in my games (<a className={"text-link"} href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"} target={"_blank"} rel="noreferrer">Photon Engineer</a>) are flawlessly balanced, I have gained valuable experience by making mistakes, which I am eager to share with you.</p>

                        <div className={"blog-sub-title"}>Game details</div>
                        <p>First of all, I'd like to provide some key details about my puzzle game for those who haven't played it.</p>
                        <p>The game offers two distinct categories of puzzles, each with a unique challenge to assess their complexity.</p>

                        <p><b>Laser puzzles:</b></p>
                        <p><img className={"blog-img"} src={blogPuzzlesComplexityLaserPuzzleImg} width={600} height={337} alt={"laser puzzle - Photon Engineer"}/></p>
                        <p>The player can build different blocks, each with unique features such as delaying a laser or pushing another block. The objective of the game is to redirect lasers into receptors and match a specific pattern.</p>

                        <p><b>Sliding puzzles:</b></p>
                        <p><img className={"blog-img"} src={blogPuzzlesComplexitySlidingPuzzleImg} width={600} height={337} alt={"sliding puzzle - Photon Engineer"}/></p>
                        <p>This is a 2D puzzle game where the objective is to use the red pieces to move the yellow piece to its target.</p>

                        <div className={"blog-sub-title"}>Technique #1: self-evaluation</div>
                        <p>Self-evaluating puzzles as a solo game developer is clearly not an easy or effective task. However, depending on the puzzle type, it is still possible to make a rough evaluation of its difficulty.</p>
                        <p>I found two techniques to make an approximate evaluation of the difficulty for the <b>sliding puzzles</b>:</p>
                        <ul>
                            <li>Sliding puzzles require a certain number of moves to complete, which can be a useful but imperfect indicator of their difficulty. The number of pieces that can be moved also contributes to the difficulty of the puzzle: the more possibilities there are, the more complex the puzzle is likely to be.</li>
                            <li>It took me about two years to develop the game. As a result, I had the opportunity to partially forget the solutions to the early puzzles and experience them as if they were designed by someone else.</li>
                        </ul>
                        <p></p>
                        <p>However, both of these techniques do not work with laser puzzles. This is because this type of puzzle offers an infinite number of possibilities to place the blocks. Furthermore, I found that my brain remembered <b>laser puzzles</b> better than <b>sliding puzzles</b>, perhaps because they are 3D and took me longer to create.</p>

                        <div className={"blog-sub-title"}>Technique #2: find players</div>
                        <p>One obvious way to evaluate the difficulty of puzzles is to have people play the game and provide feedback on each puzzle. This technique is probably one of the best, but it can be difficult to implement. Since this is my first game, the players don't know me and most of them are not interested in playing a game in development. Additionally, it may take some time for them to provide feedback.</p>
                        <p>I found some people but I had the feeling that it was not sufficient to have a good indicator of the complexity of all my puzzles.</p>

                        <div className={"blog-sub-title"}>Technique #3: gather statistics</div>
                        <p>Instead of requesting feedback from players about the difficulty of a puzzle, I came up with a new idea: add timing statistics. I implemented a time measurement feature to track the time taken to solve each puzzle, and then sent the statistics to a server. Finally, I aggregated the results to gain a clearer understanding of the difficulties of each puzzle.</p>
                        <p>Here is the result for one puzzle of the level 15:</p>
                        <p><img className={"blog-img"} src={blogPuzzlesComplexityStats1Img} width={600} height={349} alt={"Puzzle statistics 1 - Photon Engineer"}/></p>
                        <p>This graph shows that 1 player found the solution to the puzzle in 3 min, 3 players found the solution in 4 minutes, etc.</p>

                        <p>Here is another graph:</p>
                        <p><img className={"blog-img"} src={blogPuzzlesComplexityStats2Img} width={600} height={349} alt={"Puzzle statistics 2 - Photon Engineer"}/></p>
                        <p>This graph shows a significant difference in the resolution time of the puzzle in level 20. It appears that some players can find the solution in about 9 minutes while others take more than 30 minutes. This result is concerning and indicates that I should review this particular puzzle.</p>

                        <p>I was quite pleased with these statistics and believed that it would be the perfect solution to evaluate the complexity of the puzzles in my game. However, I later discovered that I was mistaken...</p>

                        <div className={"blog-sub-title"}>Technique #4: gameplay recording</div>
                        <p>When my game was almost finished, some people offered to test it and eventually record videos of their gameplay if I need them. I didn't ask the players to record my game since I felt it would take up their time and be unnecessary given that I already had statistics recording.</p>
                        <p>Fortunately, some generous testers recorded gameplay footage and I discovered a major flaw in my statistics. It became clear that around 50% of the players were struggling with rotating the blocks in the 3D world and not with solving the puzzle. To provide context, players have the ability to rotate a block 90 degrees on the horizontal, vertical and depth axes. Through these recordings, I realized that not all players were able to understand how the rotation worked and were randomly trying to rotate the blocks in any direction, hoping to get the correct orientation.</p>

                        <p><img className={"blog-img"} src={blogPuzzlesComplexityBlockRotationImg} width={600} height={349} alt={"Block rotation - Photon Engineer"}/></p>

                        <p>Essentially, the gameplay issue with the block rotation in my 3D laser puzzles interfered with all the statistics I had gathered.</p>
                        <p><u>Note</u>: I finally fixed this rotation problem with a better tutorial and a new system to make the block run smarter.</p>

                        <div className={"blog-sub-title"}>Conclusion</div>
                        <p>There isn't a one-size-fits-all technique to evaluate the difficulty of a puzzle. I've found that a combination of statistics to gather a global perspective, and gameplay recordings to gather more detailed feedback from a smaller group of players, works best for me.</p>

                        <p></p>
                    </div>
                </div>
            </div>
        );
    }
}

export default PuzzlesComplexity;
