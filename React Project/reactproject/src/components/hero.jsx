const Hero = () => {
    return (
        <main className="hero">
            <div className="hero_content">
                <h1> Your feet Deserve <span>the Best.</span></h1>
                <p className="prag">Your feet deserve the best and You should look like super rich.<br></br>So that we are here to help you to find the best shoe that fits on you.</p>

                <div className="hero_button">
                    <button className="one">Shop Now</button>
                    <button className="two">Category</button>
                </div>
                <div className="detail">
                    <p>Also available in Following app: </p>
                    <img src="/images/amazon_logo.jpg" className="img1"></img>
                    <img src="/images/walmart_logo.png"></img>
                </div>
            </div>
            <div className="hero_img">
                <img src="https://www.polynesianpride.co/cdn/shop/products/front_2_2d4666d5-43aa-4c71-980d-6d6a87adcf21_1200x.jpg?v=1632884261" alt="muji"></img>

            </div>

        </main>
    )
}

export default Hero;