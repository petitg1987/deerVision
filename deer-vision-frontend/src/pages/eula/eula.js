import React, {Component} from 'react';
import "../pages.css"
import "./eula.css"
import Description from "../../components/description/description";

class Eula extends Component {

    render() {
        return (
            <div>
                <h2>End User License Agreement: Games</h2>
                <div className="eula-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <div className="eula-description">
                        <Description>
                            <p>Last update: 24th of January 2022</p>
                        </Description>

                    </div>
                </div>
            </div>)
    }
}

export default Eula;
