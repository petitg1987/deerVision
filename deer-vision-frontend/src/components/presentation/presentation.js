import React, {Component} from 'react';
import './presentation.css';
import studioLogo from "../../images/studioLogo.png";
import Description from "../description/description";

class Presentation extends Component {

    render() {
        return (
            <div className="pres-container">
                <div className="pres-separator"/>
                <div className="pres-description">
                    <Description/>
                </div>
                <div className="pres-separator"/>
                <div className="pres-logo">
                    <img src={studioLogo} alt="Studio Logo" width="300" height="300"/>
                </div>
            </div>
        );
    }
}

export default Presentation;
