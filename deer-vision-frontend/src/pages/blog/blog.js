import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionImg from "../../images/blog/blogConsumption.webp"
import blogBalancingImg from "../../images/blog/blogBalancing.webp"

class Blog extends Component {
    render() {
        return (
            <div>
                <h2>Blog</h2>
                <div className="blog-container">

                    <div className="blog-preview">
                        <div className={"blog-title"}><a className={"text-link"} href={"/blog/balancing-puzzles"}>The science of difficulty: how I balanced puzzles in my game</a></div>
                        <div className={"blog-preview-main"}>
                            <img className={"blog-img"} src={blogBalancingImg} width={130} height={130} alt={"Puzzle made of wood"}/>
                            <div>If you're a fan of puzzle games and curious about the behind-the-scenes work of game development, this blog is for you. Discover how I tackled the challenge of balancing puzzles in my game.</div>
                        </div>
                        <div className={"blog-metadata"}>date: 2023-04-19 | author: Grégory Petit</div>
                    </div>

                    <div className="blog-preview">
                        <div className={"blog-title"}><a className={"text-link"} href={"/blog/consumption"}>I measured the electricity consumption of my game</a></div>
                        <div className={"blog-preview-main"}>
                            <img className={"blog-img"} src={blogConsumptionImg} width={130} height={130} alt={"Device to measure electricity consumption"}/>
                            <div>As a programmer, I always wondered what difference an optimized application could make on the amount of electricity consumed by a computer. Therefore, I decided to measure the power consumption of my own puzzle game.</div>
                        </div>
                        <div className={"blog-metadata"}>date: 2023-03-03 | author: Grégory Petit</div>
                    </div>

                </div>
            </div>
        );
    }
}

export default Blog;
