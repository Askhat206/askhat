(function () {
    //Login/Signup modal window - by CodyHouse.co
    function ModalSignin(element) {
        this.element = element;
        this.blocks = this.element.getElementsByClassName('js-signin-modal-block');
        this.switchers = this.element.getElementsByClassName('js-signin-modal-switcher')[0].getElementsByTagName('a');
        this.triggers = document.getElementsByClassName('js-signin-modal-trigger');
        this.hidePassword = this.element.getElementsByClassName('js-hide-password');
        this.init();
    };

    ModalSignin.prototype.init = function () {
        var self = this;

        for (var i = 0; i < this.triggers.length; i++) {
            (function (i) {
                self.triggers[i].addEventListener('click', function (event) {
                    if (event.target.hasAttribute('data-signin')) {
                        event.preventDefault();
                        self.showSigninForm(event.target.getAttribute('data-signin'));
                    }
                });
            })(i);
        }

        //close modal
        this.element.addEventListener('click', function (event) {
            if (hasClass(event.target, 'js-signin-modal') || hasClass(event.target, 'js-close')) {
                event.preventDefault();
                removeClass(self.element, 'cd-signin-modal--is-visible');
            }
        });
        //close modal when clicking the esc keyboard button
        document.addEventListener('keydown', function (event) {
            (event.which == '27') && removeClass(self.element, 'cd-signin-modal--is-visible');
        });

        //hide/show password
        for (var i = 0; i < this.hidePassword.length; i++) {
            (function (i) {
                self.hidePassword[i].addEventListener('click', function (event) {
                    self.togglePassword(self.hidePassword[i]);
                });
            })(i);
        }

        //IMPORTANT - REMOVE THIS - it's just to show/hide error messages in the demo
        this.blocks[0].getElementsByTagName('form')[0].addEventListener('submit', function (event) {
            event.preventDefault();
            self.toggleError(document.getElementById('signin-email'), true);
        });
        this.blocks[1].getElementsByTagName('form')[0].addEventListener('submit', function (event) {
            event.preventDefault();
            self.toggleError(document.getElementById('signup-username'), true);
        });
    };

    ModalSignin.prototype.togglePassword = function (target) {
        var password = target.previousElementSibling;
        ('password' == password.getAttribute('type')) ? password.setAttribute('type', 'text') : password.setAttribute('type', 'password');
        target.textContent = ('Hide' == target.textContent) ? 'Show' : 'Hide';
        putCursorAtEnd(password);
    }

    ModalSignin.prototype.showSigninForm = function (type) {
        // show modal if not visible
        !hasClass(this.element, 'cd-signin-modal--is-visible') && addClass(this.element, 'cd-signin-modal--is-visible');
        // show selected form
        for (var i = 0; i < this.blocks.length; i++) {
            this.blocks[i].getAttribute('data-type') == type ? addClass(this.blocks[i], 'cd-signin-modal__block--is-selected') : removeClass(this.blocks[i], 'cd-signin-modal__block--is-selected');
        }
        //update switcher appearance
        var switcherType = (type == 'signup') ? 'signup' : 'login';
        for (var i = 0; i < this.switchers.length; i++) {
            this.switchers[i].getAttribute('data-type') == switcherType ? addClass(this.switchers[i], 'cd-selected') : removeClass(this.switchers[i], 'cd-selected');
        }
    };

    ModalSignin.prototype.toggleError = function (input, bool) {
        // used to show error messages in the form
        toggleClass(input, 'cd-signin-modal__input--has-error', bool);
        toggleClass(input.nextElementSibling, 'cd-signin-modal__error--is-visible', bool);
    }

    var signinModal = document.getElementsByClassName("js-signin-modal")[0];
    if (signinModal) {
        new ModalSignin(signinModal);
    }

    // toggle main navigation on mobile
    var mainNav = document.getElementsByClassName('js-main-nav')[0];
    if (mainNav) {
        mainNav.addEventListener('click', function (event) {
            if (hasClass(event.target, 'js-main-nav')) {
                var navList = mainNav.getElementsByTagName('ul')[0];
                toggleClass(navList, 'cd-main-nav__list--is-visible', !hasClass(navList, 'cd-main-nav__list--is-visible'));
            }
        });
    }

    //class manipulations - needed if classList is not supported
    function hasClass(el, className) {
        if (el.classList) return el.classList.contains(className);
        else return !!el.className.match(new RegExp('(\\s|^)' + className + '(\\s|$)'));
    }

    function addClass(el, className) {
        var classList = className.split(' ');
        if (el.classList) el.classList.add(classList[0]);
        else if (!hasClass(el, classList[0])) el.className += " " + classList[0];
        if (classList.length > 1) addClass(el, classList.slice(1).join(' '));
    }

    function removeClass(el, className) {
        var classList = className.split(' ');
        if (el.classList) el.classList.remove(classList[0]);
        else if (hasClass(el, classList[0])) {
            var reg = new RegExp('(\\s|^)' + classList[0] + '(\\s|$)');
            el.className = el.className.replace(reg, ' ');
        }
        if (classList.length > 1) removeClass(el, classList.slice(1).join(' '));
    }

    function toggleClass(el, className, bool) {
        if (bool) addClass(el, className);
        else removeClass(el, className);
    }

    //credits http://css-tricks.com/snippets/jquery/move-cursor-to-end-of-textarea-or-input/
    function putCursorAtEnd(el) {
        if (el.setSelectionRange) {
            var len = el.value.length * 2;
            el.focus();
            el.setSelectionRange(len, len);
        } else {
            el.value = el.value;
        }
    };
})();

function onEnter(element) {
    setTimeout(function () {
        let email = element.value;
        console.log(email)
        location.assign(`http://localhost:8080/products/search?search=${email}`)
    }, 1500)
}

function sendSms() {
    let smsSubmit = document.querySelector(".login__submit__form")
    let smsSend = document.querySelector(".login__form")
    console.log(smsSubmit)
    console.log(smsSend)
    smsSend.style.display = 'none'
    smsSubmit.style.display = 'block'
    let email = document.getElementById("emailInput").value;
    const url = `http://localhost:8080/api/emails`
    // let xhr = new XMLHttpRequest();
    // xhr.open("POST", url);
    // xhr.send();
    // xhr.onload = () => console.log(xhr.responseText);


    fetch('http://localhost:8080/api/emails', {
        method: 'POST',
        headers: {
            'Accept': '*/*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: `${email}`,
            smsRequestType: 'REGISTER',
        })
    })
        .then(response => {
            if (!response.ok || true) {
                throw new Error('Failed to submit SMS code');
            }
            // Handle success response here
            console.log('SMS code submitted successfully');
        })
        .catch(error => {
            // Handle error here
            console.error('Error:', error);
        });

}

function submit() {
    let code = document.querySelector("#sms_code").value;
    let email = document.getElementById("emailInput").value;
    console.log(email);
    console.log(code);

    fetch('http://localhost:8080/api/emails/verify', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            verificationCode: code,
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse the JSON data
        })
        .then(data => {
            console.log('Response:', data);
            let token = JSON.stringify(data)
            console.log('================')
            console.log(data.refreshToken)
            console.log(data.accessToken)
            document.cookie = "refreshToken=" + data.refreshToken
            console.log('================')
            sessionStorage.setItem("refresh-token", data.refreshToken)
            sessionStorage.setItem("access-token", data.accessToken)
            location.reload()
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while processing your request. Please try again later.');
        });

}


function openModal(id) {
    console.log('aa')
    getSubCategories(id)
    let modalElement = document.getElementById('temporaryModal');
    let modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();

    let backdropElement = document.querySelector('.modal-backdrop');
    if (backdropElement) {
        backdropElement.remove();
    }

    modalElement.style.width = '65%';
    modalElement.style.height = '75%';
    modalElement.style.marginTop = '10%';
    modalElement.style.marginLeft = '25%';

    // modalElement.style.backgroundColor = '#cb0000';
    // modalElement.style.opacity = '0.5'
    modalElement.style.backgroundColor = 'rgba(0, 0, 0, 0.5)'

}

function closeModal() {

    let modalInstance = new bootstrap.Modal(document.getElementById('temporaryModal'));
    modalInstance._hideModal();
}

function getSubCategories(id) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/subcategory/" + id, true);
    http.setRequestHeader("Content-type", "application/json");

    http.onreadystatechange = function () {
        if (http.readyState === XMLHttpRequest.DONE && http.status === 200) {
            let subCategories = JSON.parse(http.response);

            let modalBody = document.querySelector('.modal-body');
            modalBody.innerHTML = '';

            let body = '';

            for (let i = 0; i < subCategories.length; i++) {
                let name = subCategories[i].name;
                // Correctly concatenate the subcategory id with the string using +
                body += "<a onclick=\"openCategories('" +
                    subCategories[i].id + "')\" class='subcategory-name'>" + name + "</a>";
            }


            modalBody.innerHTML = body;
        }
    };


    http.send();
}

function openCategories(id) {
    window.open("/products/get/" + id)

}

function addToFavorite(productId) {


}