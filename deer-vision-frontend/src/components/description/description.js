import React, {Component} from 'react';
import './description.css';

class Description extends Component {

    render() {
        return (
            <div className="desc-text">
                {this.props.children}
            </div>
        );
    }
}

export default Description;
