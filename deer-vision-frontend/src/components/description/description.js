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
        const paragraphes = document.querySelectorAll('.desc-text p');
        paragraphes.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <div className="desc-text">
                {this.props.texts}
            </div>
        );
    }
}

export default Description;
