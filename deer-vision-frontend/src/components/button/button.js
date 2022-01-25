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
        const paragraphs = document.querySelectorAll('.btn-text');
        paragraphs.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <a href={this.props.link} target="_blank" rel="noopener noreferrer" className="btn">
                <div className="btn-text"><img alt="Logo" src={this.props.squareLogo} width="20" height="20"/> {this.props.text}</div>
            </a>
        );
    }
}

export default Button;
