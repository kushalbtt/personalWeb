let pageTransitionOverlay = null;

// Load section content
async function loadSection(sectionId) {
    try {
        const response = await fetch(`${sectionId}.html`);
        const sectionElement = document.getElementById(`${sectionId}-section`);
        if (!sectionElement) {
            console.error(`Section element not found: ${sectionId}-section`);
            return false;
        }

        if (!response.ok) {
            console.error(`Failed to load ${sectionId}: ${response.status} ${response.statusText}`);
            sectionElement.innerHTML = `<div class="load-error">Failed to load <strong>${sectionId}</strong>. Try serving the site using a local server (e.g. <code>python3 -m http.server</code>) and open <code>http://localhost:8000</code>.</div>`;
            return false;
        }

        const content = await response.text();
        sectionElement.innerHTML = content;
        console.log(`Loaded section: ${sectionId}`);
        return true;
    } catch (error) {
        console.error(`Error loading ${sectionId}:`, error);
        const sectionElement = document.getElementById(`${sectionId}-section`);
        if (sectionElement) {
            sectionElement.innerHTML = `<div class="load-error">Error loading <strong>${sectionId}</strong>. Check the browser console. If you're opening this file directly (file://), serve the folder with a local HTTP server.</div>`;
        }
        return false;
    }
}

function getNavbarHeight() {
    const navbar = document.querySelector('.navbar');
    return navbar ? navbar.offsetHeight : 0;
}

async function ensureSectionReady(sectionId) {
    const sectionElement = document.getElementById(`${sectionId}-section`);
    if (!sectionElement) {
        console.error(`Section container missing for ${sectionId}`);
        return null;
    }

    if (!sectionElement.innerHTML.trim()) {
        const loaded = await loadSection(sectionId);
        if (!loaded) return null;
    }

    const targetSection = sectionElement.querySelector(`section#${sectionId}`);
    if (!targetSection) {
        console.error('Target section not found in DOM:', sectionId);
        return null;
    }

    return targetSection;
}

async function runWithPageTransition(sectionId, task) {
    if (!pageTransitionOverlay) {
        await task();
        return;
    }

    return new Promise(resolve => {
        pageTransitionOverlay.setAttribute('data-target', sectionId || '');
        pageTransitionOverlay.classList.add('active');
        setTimeout(async () => {
            await task();
            setTimeout(() => {
                pageTransitionOverlay.classList.remove('active');
                resolve();
            }, 400);
        }, 200);
    });
}

async function navigateToSection(sectionId) {
    if (!sectionId) return;
    await runWithPageTransition(sectionId, async () => {
        const targetSection = await ensureSectionReady(sectionId);
        if (!targetSection) return;
        const targetOffset = targetSection.offsetTop - getNavbarHeight();
        window.scrollTo({
            top: targetOffset,
            behavior: 'smooth'
        });
        history.replaceState(null, '', `#${sectionId}`);
    });
}

// Handle smooth scrolling
async function handleScrollLink(e) {
    e.preventDefault();
    const targetId = this.getAttribute('href');
    if (!targetId || !targetId.startsWith('#')) return;
    const sectionId = targetId.substring(1);
    await navigateToSection(sectionId);
}

// Add event listeners to scroll links
function addScrollLinkListeners() {
    const scrollLinks = document.querySelectorAll('.scroll-link');
    console.log('Adding listeners to scroll links:', scrollLinks.length);
    
    scrollLinks.forEach(link => {
        // Remove any existing listeners to prevent duplicates
        link.removeEventListener('click', handleScrollLink);
        link.addEventListener('click', handleScrollLink);
        console.log('Added click listener to:', link.getAttribute('href'));
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
        submitBtn.innerHTML = `<i class="fas fa-spinner fa-spin"></i> Sending...`;

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
            submitBtn.innerHTML = `<span class="btn-text">Send Message</span><i class="fas fa-paper-plane"></i>`;
            setTimeout(() => {
                formStatus.style.display = 'none';
            }, 4000);
        });
    });
}

// Load all sections
document.addEventListener('DOMContentLoaded', async function() {
    console.log('DOM Content Loaded');
    const sectionIds = ['home', 'about', 'experience', 'projects', 'contact'];

    pageTransitionOverlay = document.createElement('div');
    pageTransitionOverlay.className = 'page-transition-overlay';
    document.body.appendChild(pageTransitionOverlay);
    
    // Add click event listeners to all navigation links
    document.querySelectorAll('.nav-links a[href^="#"]').forEach(link => {
        link.addEventListener('click', handleScrollLink);
    });

    // Load sections
    for (const sectionId of sectionIds) {
        const loaded = await loadSection(sectionId);
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
        const targetSection = await ensureSectionReady(sectionId);
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

    // Add a mutation observer to watch for dynamic content changes
    const contentObserver = new MutationObserver((mutations) => {
        // Only add listeners when newly added nodes actually contain scroll links
        let shouldAdd = false;
        mutations.forEach((mutation) => {
            if (mutation.addedNodes.length) {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1) { // ELEMENT_NODE
                        if (node.classList && node.classList.contains('scroll-link')) {
                            shouldAdd = true;
                        } else if (node.querySelector && node.querySelector('.scroll-link')) {
                            shouldAdd = true;
                        }
                    }
                });
            }
        });
        if (shouldAdd) addScrollLinkListeners();
    });

    // Start observing the document with the configured parameters
    contentObserver.observe(document.body, { childList: true, subtree: true });

    // Add scroll-based animations
    const sections = document.querySelectorAll('.section');
    const options = {
        threshold: 0.1
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, options);

    sections.forEach(section => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        section.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        observer.observe(section);
    });

    // Mobile menu toggle
    const createMobileMenu = () => {
        const navbar = document.querySelector('.navbar');
        const navLinks = document.querySelector('.nav-links');
        
        const menuButton = document.createElement('button');
        menuButton.classList.add('menu-button');
        menuButton.innerHTML = '<i class="fas fa-bars"></i>';
        
        navbar.appendChild(menuButton);
        
        menuButton.addEventListener('click', () => {
            navLinks.classList.toggle('active');
        });
    };

    // Initialize mobile menu if needed
    if (window.innerWidth <= 768) {
        createMobileMenu();
    }
});
