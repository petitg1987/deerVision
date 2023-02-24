import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionImg from "../../images/blogConsumption.webp"

class Blog extends Component {
    render() {
        return (
            <div>
                <h2>Blog articles</h2>
                <div className="blog-container">
                    <div className="blog-preview">
                        <div className={"blog-title"}><a className={"text-link"} href={"/blog/consumption"}>I measured the electricity consumption of my game</a></div>
                        <div className={"blog-preview-main"}>
                            <img className={"blog-img"} src={blogConsumptionImg} width={130} height={130} alt={"Device to measure electricity consumption"}/>
                            <div>As a programmer, I've always wondered how much difference an optimized application can make to the electricity consumed by a computer. What's better than testing on my own puzzle game?</div>
                        </div>
                        <div className={"blog-metadata"}>date: 2023-02-24, poster: Grégory Petit</div>
                    </div>

                    <div className="blog-preview">
                        <small>More blogs are coming soon...</small>
                    </div>
                </div>
            </div>
        );
    }
}

export default Blog;
