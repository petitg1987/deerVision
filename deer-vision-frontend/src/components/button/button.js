import React, {Component} from 'react';
import './button.css';

class Button extends Component {

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('btn-text-anim-trigger');
                } else {
                    entry.target.classList.remove('btn-text-anim-trigger');
                }
            });
        });
        const paragraphes = document.querySelectorAll('.btn-text');
        paragraphes.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <a href={this.props.link} target="_blank" rel="noopener noreferrer" className="btn">
                <div className="btn-text">{this.props.text}</div>
            </a>
        );
    }
}

export default Button;
