function getTemplateMarkup(sectionId) {
    const template = document.getElementById(`${sectionId}-template`);
    return template ? template.innerHTML.trim() : '';
}

// Load section content
function loadSection(sectionId) {
    const sectionElement = document.getElementById(`${sectionId}-section`);

    if (!sectionElement) {
        console.error(`Section element not found: ${sectionId}-section`);
        return false;
    }

    if (sectionElement.innerHTML.trim()) {
        return true;
    }

    const templateMarkup = getTemplateMarkup(sectionId);
    if (templateMarkup) {
        sectionElement.innerHTML = templateMarkup;
        return true;
    }

    sectionElement.innerHTML = `<div class="load-error">Failed to load <strong>${sectionId}</strong>.</div>`;
    return false;
}

function getNavbarHeight() {
    const navbar = document.querySelector('.navbar');
    return navbar ? navbar.offsetHeight : 0;
}

function ensureSectionReady(sectionId) {
    const sectionElement = document.getElementById(`${sectionId}-section`);
    if (!sectionElement) {
        console.error(`Section container missing for ${sectionId}`);
        return null;
    }

    if (!sectionElement.innerHTML.trim()) {
        const loaded = loadSection(sectionId);
        if (!loaded) return null;
    }

    const targetSection = sectionElement.querySelector(`section#${sectionId}`);
    if (!targetSection) {
        console.error('Target section not found in DOM:', sectionId);
        return null;
    }

    return targetSection;
}

function navigateToSection(sectionId) {
    if (!sectionId) return;
    const targetSection = ensureSectionReady(sectionId);
    if (!targetSection) return;
    const targetOffset = targetSection.offsetTop - getNavbarHeight();
    window.scrollTo({
        top: targetOffset,
        behavior: 'smooth'
    });
    history.replaceState(null, '', `#${sectionId}`);
}

// Handle smooth scrolling
function handleScrollLink(e) {
    e.preventDefault();
    const targetId = this.getAttribute('href');
    if (!targetId || !targetId.startsWith('#')) return;
    const sectionId = targetId.substring(1);
    navigateToSection(sectionId);
}

// Add event listeners to scroll links
function addScrollLinkListeners() {
    const scrollLinks = document.querySelectorAll('.scroll-link');
    
    scrollLinks.forEach(link => {
        link.removeEventListener('click', handleScrollLink);
        link.addEventListener('click', handleScrollLink);
    });
}

// Form submission handling
function initializeContactForm() {
    const contactForm = document.getElementById('contactForm');
    if (!contactForm) return;

    contactForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const form = e.target;
        const submitBtn = document.getElementById('submitBtn');
        const formStatus = document.getElementById('formStatus');
        const formData = new FormData(form);

        submitBtn.disabled = true;
        submitBtn.textContent = 'Sending...';

        fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        }).then(response => {
            if (response.ok) {
                formStatus.innerHTML = `<div class="success-message">Message sent successfully!</div>`;
                formStatus.style.display = 'block';
                form.reset();
            } else {
                response.json().then(data => {
                    formStatus.innerHTML = `<div class="error-message">${data.errors.map(error => error.message).join(", ")}</div>`;
                    formStatus.style.display = 'block';
                })
            }
        }).catch(error => {
            formStatus.innerHTML = `<div class="error-message">Oops! There was a problem submitting your form.</div>`;
            formStatus.style.display = 'block';
        }).finally(() => {
            submitBtn.disabled = false;
            submitBtn.innerHTML = `<span class="btn-text">Send Message</span>`;
            setTimeout(() => {
                formStatus.style.display = 'none';
            }, 4000);
        });
    });
}

function loadBackgroundVideo() {
    const video = document.querySelector('.video-background video[data-src]');
    if (!video) return;

    const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;
    if (connection && (connection.saveData || /2g/.test(connection.effectiveType || ''))) return;

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

    const source = document.createElement('source');
    source.src = video.dataset.src;
    source.type = 'video/mp4';
    video.appendChild(source);
    video.removeAttribute('data-src');
    video.load();
    video.play().catch(() => {});
}

function loadAnalytics() {
    if (window.gtag) return;

    window.dataLayer = window.dataLayer || [];
    window.gtag = function gtag() {
        window.dataLayer.push(arguments);
    };

    const script = document.createElement('script');
    script.async = true;
    script.src = 'https://www.googletagmanager.com/gtag/js?id=G-PTND45BEX7';
    document.head.appendChild(script);

    window.gtag('js', new Date());
    window.gtag('config', 'G-PTND45BEX7');
}

// Load all sections
document.addEventListener('DOMContentLoaded', function() {
    const sectionIds = ['home', 'about', 'experience', 'projects', 'contact'];
    
    // Add click event listeners to all navigation links
    document.querySelectorAll('.nav-links a[href^="#"]').forEach(link => {
        link.addEventListener('click', handleScrollLink);
    });

    // Load sections
    for (const sectionId of sectionIds) {
        const loaded = loadSection(sectionId);
        if (!loaded) {
            console.error(`Failed to load section ${sectionId}`);
        }
    }

    // Add scroll link listeners after all content is loaded
    addScrollLinkListeners();
    initializeContactForm();

    // Handle initial page load with hash
    if (window.location.hash) {
        const sectionId = window.location.hash.substring(1);
        const targetSection = ensureSectionReady(sectionId);
        if (targetSection) {
            const targetOffset = targetSection.offsetTop - getNavbarHeight();
            window.scrollTo({
                top: targetOffset,
                behavior: 'smooth'
            });
        }
    }

    // Update active navigation link based on scroll position
    function updateActiveNavLink() {
        const sections = document.querySelectorAll('section');
        const navLinks = document.querySelectorAll('.nav-links a');
        
        sections.forEach(section => {
            const navbar = document.querySelector('.navbar');
            const navbarHeight = navbar.offsetHeight;
            const sectionTop = section.offsetTop - navbarHeight - 100;
            const sectionBottom = sectionTop + section.offsetHeight;
            const scrollPosition = window.scrollY;
            
            if (scrollPosition >= sectionTop && scrollPosition < sectionBottom) {
                const targetId = '#' + section.id;
                navLinks.forEach(link => {
                    link.classList.remove('active');
                    if (link.getAttribute('href') === targetId) {
                        link.classList.add('active');
                    }
                });
            }
        });
    }

    // Add scroll event listener for updating active link
    window.addEventListener('scroll', updateActiveNavLink);
    updateActiveNavLink(); // Initial call

    // Mobile menu toggle
    const createMobileMenu = () => {
        const navbar = document.querySelector('.navbar');
        const navLinks = document.querySelector('.nav-links');
        
        const menuButton = document.createElement('button');
        menuButton.classList.add('menu-button');
        menuButton.type = 'button';
        menuButton.setAttribute('aria-label', 'Toggle navigation');
        menuButton.textContent = 'Menu';
        
        navbar.appendChild(menuButton);
        
        menuButton.addEventListener('click', () => {
            navLinks.classList.toggle('active');
        });
    };

    // Initialize mobile menu if needed
    if (window.innerWidth <= 768) {
        createMobileMenu();
    }

    ['pointerdown', 'keydown', 'scroll'].forEach(eventName => {
        window.addEventListener(eventName, loadBackgroundVideo, { once: true, passive: true });
        window.addEventListener(eventName, loadAnalytics, { once: true, passive: true });
    });
});
