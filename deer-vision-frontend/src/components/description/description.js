import React, {Component} from 'react';
import './description.css';

class Description extends Component {

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('desc-text-p-anim-trigger');
                } else {
                    entry.target.classList.remove('desc-text-p-anim-trigger');
                }
            });
        });
        const paragraphs = document.querySelectorAll('.desc-text p');
        paragraphs.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <div className="desc-text">
                {this.props.children}
            </div>
        );
    }
}

export default Description;
