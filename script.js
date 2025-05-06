// Load section content
async function loadSection(sectionId) {
    try {
        const response = await fetch(`${sectionId}.html`);
        if (!response.ok) throw new Error(`Failed to load ${sectionId}`);
        const content = await response.text();
        const sectionElement = document.getElementById(`${sectionId}-section`);
        if (!sectionElement) {
            console.error(`Section element not found: ${sectionId}-section`);
            return;
        }
        sectionElement.innerHTML = content;
        console.log(`Loaded section: ${sectionId}`);
        return true;
    } catch (error) {
        console.error(`Error loading ${sectionId}:`, error);
        return false;
    }
}

// Handle smooth scrolling
async function handleScrollLink(e) {
    e.preventDefault();
    const targetId = this.getAttribute('href');
    console.log('Scroll link clicked:', targetId);
    
    // Extract section ID from href (e.g., "#contact" -> "contact")
    const sectionId = targetId.substring(1);
    
    // Ensure the target section is loaded
    const sectionElement = document.getElementById(`${sectionId}-section`);
    if (!sectionElement || !sectionElement.innerHTML.trim()) {
        console.log(`Section ${sectionId} not loaded, loading now...`);
        await loadSection(sectionId);
    }
    
    // Find the section element within the loaded content
    const targetSection = sectionElement.querySelector(`section#${sectionId}`);
    if (!targetSection) {
        console.error('Target section not found:', targetId);
        return;
    }

    const navbar = document.querySelector('.navbar');
    if (!navbar) {
        console.error('Navbar not found');
        return;
    }

    const navbarHeight = navbar.offsetHeight;
    const targetOffset = targetSection.offsetTop - navbarHeight;
    
    console.log('Scrolling to:', targetId, 'at offset:', targetOffset);
    window.scrollTo({
        top: targetOffset,
        behavior: 'smooth'
    });
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

// Load all sections
document.addEventListener('DOMContentLoaded', async function() {
    console.log('DOM Content Loaded');
    const sectionIds = ['home', 'about', 'experience', 'projects', 'contact'];
    
    // Add click event listeners to all navigation links
    document.querySelectorAll('.nav-links a[href^="#"]').forEach(link => {
        link.addEventListener('click', handleScrollLink);
    });

    // Load sections
    for (const sectionId of sectionIds) {
        await loadSection(sectionId);
    }

    // Add scroll link listeners after all content is loaded
    addScrollLinkListeners();

    // Handle initial page load with hash
    if (window.location.hash) {
        const sectionId = window.location.hash.substring(1);
        const sectionElement = document.getElementById(`${sectionId}-section`);
        if (sectionElement) {
            const targetSection = sectionElement.querySelector(`section#${sectionId}`);
            if (targetSection) {
                setTimeout(() => {
                    const navbar = document.querySelector('.navbar');
                    const navbarHeight = navbar.offsetHeight;
                    const targetOffset = targetSection.offsetTop - navbarHeight;
                    window.scrollTo({
                        top: targetOffset,
                        behavior: 'smooth'
                    });
                }, 100);
            }
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
        mutations.forEach((mutation) => {
            if (mutation.addedNodes.length) {
                addScrollLinkListeners();
            }
        });
    });

    // Start observing the document with the configured parameters
    contentObserver.observe(document.body, { childList: true, subtree: true });

    // Form submission handling
    document.getElementById('contactForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const submitBtn = document.getElementById('submitBtn');
        const formStatus = document.getElementById('formStatus');
        const btnText = submitBtn.querySelector('.btn-text');
        
        // Disable submit button and show loading state
        submitBtn.disabled = true;
        btnText.textContent = 'Sending...';
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i><span class="btn-text">Sending...</span>';
        
        // Create a hidden iframe for form submission
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        document.body.appendChild(iframe);
        
        // Submit the form to the iframe
        this.target = 'form-submit-iframe';
        this.submit();
        
        // Show success message
        formStatus.innerHTML = '<div class="success-message"><i class="fas fa-check-circle"></i> Message sent successfully!</div>';
        formStatus.style.display = 'block';
        
        // Reset form
        this.reset();
        
        // Reset button state after a delay
        setTimeout(() => {
            submitBtn.disabled = false;
            btnText.textContent = 'Send Message';
            submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i><span class="btn-text">Send Message</span>';
            
            // Hide success message after 3 seconds
            formStatus.style.display = 'none';
            
            // Remove the iframe
            document.body.removeChild(iframe);
        }, 3000);
    });

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

    // Mobile menu toggle (you can add this if you want a mobile menu)
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