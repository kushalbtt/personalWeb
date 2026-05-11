// Galaxy Starfield Animation
class GalaxyBackground {
    constructor() {
        this.canvas = document.createElement('canvas');
        this.canvas.id = 'galaxy-canvas';
        this.ctx = this.canvas.getContext('2d');
        this.stars = [];
        this.shootingStars = [];
        
        this.setupCanvas();
        this.createStars();
        this.animate();
        
        window.addEventListener('resize', () => this.setupCanvas());
    }
    
    setupCanvas() {
        this.canvas.width = window.innerWidth;
        this.canvas.height = document.body.scrollHeight;
        this.canvas.style.position = 'fixed';
        this.canvas.style.top = '0';
        this.canvas.style.left = '0';
        this.canvas.style.zIndex = '-2';
        this.canvas.style.pointerEvents = 'none';
        
        if (!document.body.contains(this.canvas)) {
            document.body.insertBefore(this.canvas, document.body.firstChild);
        }
    }
    
    createStars() {
        this.stars = [];
        const starCount = Math.floor((this.canvas.width * this.canvas.height) / 3000);
        
        for (let i = 0; i < starCount; i++) {
            this.stars.push({
                x: Math.random() * this.canvas.width,
                y: Math.random() * this.canvas.height,
                radius: Math.random() * 2,
                opacity: Math.random(),
                twinkleSpeed: Math.random() * 0.02 + 0.005,
                baseOpacity: Math.random() * 0.5 + 0.3
            });
        }
    }
    
    createShootingStar() {
        if (Math.random() < 0.003) {
            this.shootingStars.push({
                x: Math.random() * this.canvas.width,
                y: Math.random() * this.canvas.height / 2,
                length: Math.random() * 80 + 40,
                speed: Math.random() * 10 + 5,
                opacity: 1,
                angle: Math.PI / 4
            });
        }
    }
    
    animate() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        // Draw twinkling stars
        this.stars.forEach(star => {
            star.opacity = star.baseOpacity + Math.sin(Date.now() * star.twinkleSpeed) * 0.3;
            this.ctx.beginPath();
            this.ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
            this.ctx.fillStyle = `rgba(255, 255, 255, ${star.opacity})`;
            this.ctx.fill();
        });
        
        // Create and draw shooting stars
        this.createShootingStar();
        this.shootingStars.forEach((star, index) => {
            const gradient = this.ctx.createLinearGradient(
                star.x, star.y,
                star.x + Math.cos(star.angle) * star.length,
                star.y + Math.sin(star.angle) * star.length
            );
            gradient.addColorStop(0, `rgba(147, 197, 253, ${star.opacity})`);
            gradient.addColorStop(1, 'rgba(147, 197, 253, 0)');
            
            this.ctx.beginPath();
            this.ctx.moveTo(star.x, star.y);
            this.ctx.lineTo(
                star.x + Math.cos(star.angle) * star.length,
                star.y + Math.sin(star.angle) * star.length
            );
            this.ctx.strokeStyle = gradient;
            this.ctx.lineWidth = 2;
            this.ctx.stroke();
            
            star.x += Math.cos(star.angle) * star.speed;
            star.y += Math.sin(star.angle) * star.speed;
            star.opacity -= 0.01;
            
            if (star.opacity <= 0) {
                this.shootingStars.splice(index, 1);
            }
        });
        
        requestAnimationFrame(() => this.animate());
    }
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => new GalaxyBackground());
} else {
    new GalaxyBackground();
}
