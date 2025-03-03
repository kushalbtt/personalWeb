const Navigation = () =>{
    return      (
        <nav>
            <div
            className="logo">
                <img src="/images/nike_logo.png" alt="logo" />
            </div>
            <ul className="button">          
                <li><button>Menu</button></li>
                <button><li href="">Location</li></button>
                <button><li href="">About</li></button>
                <button><li href="">Contact</li></button>
            </ul>

            <button>Log In</button>
        </nav>
    );

};

export default Navigation;