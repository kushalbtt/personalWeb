// Mobile Menu Toggle
const menuToggle = document.querySelector('.menu-toggle');
const navLinks = document.querySelector('.nav-links');
const navItems = document.querySelectorAll('.nav-links a');

menuToggle.addEventListener('click', () => {
    navLinks.classList.toggle('active');
});

// Close mobile menu when clicking a nav item
navItems.forEach(item => {
    item.addEventListener('click', () => {
        navLinks.classList.remove('active');
    });
});

// Smooth Scrolling
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Active Navigation Link on Scroll
const sections = document.querySelectorAll('section');
const navLinks = document.querySelectorAll('.nav-links a');

window.addEventListener('scroll', () => {
    let current = '';
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (pageYOffset >= sectionTop - 60) {
            current = section.getAttribute('id');
        }
    });

    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href').slice(1) === current) {
            link.classList.add('active');
        }
    });
});

// Typewriter Effect for Hero Title
const heroTitle = document.querySelector('.hero-title');
const text = "Hi, I'm Kushal Bhattarai";
let index = 0;

function typeWriter() {
    if (index < text.length) {
        heroTitle.innerHTML = text.substring(0, index + 1) + '<span class="highlight">|</span>';
        index++;
        setTimeout(typeWriter, 100);
    } else {
        heroTitle.innerHTML = text.substring(0, index) + '<span class="highlight">|</span>';
        setTimeout(() => {
            heroTitle.innerHTML = text;
        }, 2000);
    }
}

// Start typewriter effect when page loads
window.addEventListener('load', typeWriter);

// Project Card Animation
const projectCards = document.querySelectorAll('.project-card');

const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = '1';
            entry.target.style.transform = 'translateY(0)';
        }
    });
}, observerOptions);

projectCards.forEach(card => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(20px)';
    card.style.transition = 'all 0.5s ease-out';
    observer.observe(card);
});

// Form Validation for Contact Form
const contactForm = document.querySelector('.contact-form');
if (contactForm) {
    contactForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const message = document.getElementById('message').value;
        
        if (!name || !email || !message) {
            alert('Please fill in all fields');
            return;
        }
        
        if (!isValidEmail(email)) {
            alert('Please enter a valid email address');
            return;
        }
        
        // Here you would typically send the form data to a server
        alert('Message sent successfully!');
        contactForm.reset();
    });
}

function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Dark Mode Toggle
const darkModeToggle = document.createElement('button');
darkModeToggle.className = 'dark-mode-toggle';
darkModeToggle.innerHTML = '<i class="fas fa-moon"></i>';
document.querySelector('.navbar .container').appendChild(darkModeToggle);

darkModeToggle.addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    const icon = darkModeToggle.querySelector('i');
    icon.classList.toggle('fa-moon');
    icon.classList.toggle('fa-sun');
});

// Add dark mode styles
const style = document.createElement('style');
style.textContent = `
    .dark-mode {
        --background: #1a1a1a;
        --text-color: #ffffff;
        --light-text: #cccccc;
        --card-bg: #2d2d2d;
        --secondary-color: #1a1a1a;
    }
    
    .dark-mode .navbar {
        background-color: var(--card-bg);
    }
    
    .dark-mode .project-card {
        background-color: var(--card-bg);
    }
    
    .dark-mode .contact {
        background-color: var(--card-bg);
    }
`;
document.head.appendChild(style);

// Skills Progress Bars
const skills = document.querySelectorAll('.skill-category li');
skills.forEach(skill => {
    const progressBar = document.createElement('div');
    progressBar.className = 'progress-bar';
    skill.appendChild(progressBar);
    
    // Animate progress bar when in view
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                progressBar.style.width = '100%';
            }
        });
    }, { threshold: 0.5 });
    
    observer.observe(skill);
}); 