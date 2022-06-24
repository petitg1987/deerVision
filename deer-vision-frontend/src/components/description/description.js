import React, {Component} from 'react';
import './description.css';

class Description extends Component {

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('desc-text-anim-trigger');
                } else {
                    entry.target.classList.remove('desc-text-anim-trigger');
                }
            });
        });
        const descText = document.querySelectorAll('.desc-text');
        descText.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <div className="desc-container">
                <div className="desc-text">
                    {this.props.children}
                </div>
            </div>
        );
    }
}

export default Description;
