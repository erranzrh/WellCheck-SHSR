function validateForm() {
    let userId = document.getElementById("userId").value;
    let userFullName = document.getElementById("userFullName").value;

    if (!userId || !userFullName) {
        alert("Please fill in all required fields.");
        return false;
    }

    return true;
}

const userTable = document.getElementsByClassName('user_table');
const buttonTable = document.querySelectorAll('.admin_navBtn');

function toggleSkills() {
    let itemClass = this.className;

    for (let i = 0; i < userTable.length; i++) {
        userTable[i].className = 'user_table';
        buttonTable[i].classList.remove('admin_navBtn_active');
    }

    if (itemClass.includes('patientBtn')) {
        userTable[0].classList.add("tableActive");
    } else if (itemClass.includes('doctorBtn')) {
        userTable[1].classList.add("tableActive");
    } else if (itemClass.includes('pharmacistBtn')) {
        userTable[2].classList.add("tableActive");
    } else if (itemClass.includes('adminBtn')) {
        userTable[3].classList.add("tableActive");
    } else if (itemClass.includes('asspatBtn')) {
        userTable[4].classList.add("tableActive");
    }

    this.classList.add("admin_navBtn_active");
}

buttonTable.forEach((el) => {
    el.addEventListener('click', toggleSkills);
});

const userForm = document.getElementsByClassName('extraForm');
const radioFormInput = document.querySelectorAll('input[type=radio][name="role"]');

function toggleForm() {
    for (let i = 0; i < userForm.length; i++) {
        userForm[i].className = 'extraForm form-group';
    }

    if (this.classList.contains('patient')) {
        userForm[0].classList.add("activeForm");
    } else if (this.classList.contains('doctor')) {
        userForm[1].classList.add("activeForm");
    } else if (this.classList.contains('pharmacist')) {
        userForm[2].classList.add("activeForm");
    }
}

radioFormInput.forEach((e) => {
    e.addEventListener('click', toggleForm);
});

const confirmAddUserBtn = document.getElementById("confirmAddUserBtn"),
    cancelAddUserBtn = document.getElementById("cancelBtnAddUser"),
    addUserBtn = document.getElementById("addUserBtn"),
    deleteUserBtn = document.querySelectorAll(".deleteUserBtn"),
    confirmDeleteUserBtn = document.getElementById("confirmDeleteUserBtn"),
    cancelDeleteUserBtn = document.getElementById("cancelDeleteUserBtn"),
    editUserBtn = document.querySelectorAll(".editUserBtn");

addUserBtn.addEventListener("click", function () {
    document.querySelector(".add_user_page-title").classList.add("active-title");
    document.getElementsByClassName("add_user_page")[0].classList.add("user_page_active");
    document.getElementsByClassName("admin_main_content")[0].style.display = "none";
    document.getElementById("action").value = "add";
});

confirmAddUserBtn.addEventListener("click", function () {
    if (validateForm()) {
        const form = document.getElementById("adduser");
        form.submit();
    }
});

cancelAddUserBtn.addEventListener("click", function () {
    document.getElementsByClassName("add_user_page")[0].classList.remove("user_page_active");
    document.getElementsByClassName("admin_main_content")[0].style.display = "block";
    activeRadioForm();

    // Reset form values
    document.getElementById("userId").value = "";
    document.getElementById("userFullName").value = "";
    document.getElementById("userPassword").value = "";
    document.getElementById("userEmail").value = "";
    document.getElementById("contact").value = "";
    document.getElementById("address").value = "";
    document.getElementById("emergencyContact").value = "";
    document.getElementById("sensorId").value = "";
    document.getElementById("doctorHospital").value = "";
    document.getElementById("doctorPosition").value = "";
    document.getElementById("pharmacistHospital").value = "";
    document.getElementById("pharmacistPosition").value = "";
    document.getElementById("userId").readOnly = false;
    document.getElementById("userPassword").readOnly = false;
});

function activeRadioForm() {
    const radioFormBtn = document.querySelectorAll('.radioBtn');
    radioFormBtn.forEach((btn) => {
        btn.classList.remove("radioBtn_hide");
    });
}

function hideRadioForm() {
    const radioFormBtn = document.querySelectorAll('.radioBtn');
    radioFormBtn.forEach((btn) => {
        btn.classList.add("radioBtn_hide");
    });
}

// Delete logic
deleteUserBtn.forEach((e) => {
    e.addEventListener("click", function () {
        document.getElementsByClassName("confirmation_deleteUser_page")[0].classList.add("user_page_active");
        const row = this.closest("tr");
        document.getElementById("userIdToBeDelete").value = row.getElementsByTagName("td")[0].innerText;
        document.getElementById("userRoleToBeDelete").value = row.getElementsByTagName("td")[4].innerText;
    });
});

confirmDeleteUserBtn.addEventListener("click", function () {
    document.getElementById("deleteUserForm").submit();
});

cancelDeleteUserBtn.addEventListener("click", function () {
    document.getElementsByClassName("confirmation_deleteUser_page")[0].classList.remove("user_page_active");
    document.getElementById("userIdToBeDelete").value = "";
});

// Edit logic
editUserBtn.forEach((e) => {
    e.addEventListener("click", hideRadioForm);
    e.addEventListener("click", function () {
        const row = this.closest("tr");
        const cells = row.getElementsByTagName("td");

        document.getElementById("userId").value = cells[0].innerText;
        document.getElementById("userId").readOnly = true;
        document.getElementById("userFullName").value = cells[1].innerText;
        document.getElementById("contact").value = cells[2].innerText;
        document.getElementById("userEmail").value = cells[3].innerText;

        const editClassName = this.className;

        if (editClassName.includes('editPatient')) {
            userForm[0].classList.add("activeForm");
            radioFormInput[1].checked = true;
            document.getElementById("address").value = cells[4].innerText;
            document.getElementById("emergencyContact").value = cells[5].innerText;
            document.getElementById("sensorId").value = cells[6].innerText;
        } else if (editClassName.includes('editDoctor')) {
            userForm[1].classList.add("activeForm");
            radioFormInput[2].checked = true;
            document.getElementById("doctorHospital").value = cells[4].innerText;
            document.getElementById("doctorPosition").value = cells[5].innerText;
        } else if (editClassName.includes('editPharmacist')) {
            userForm[2].classList.add("activeForm");
            radioFormInput[3].checked = true;
            document.getElementById("pharmacistHospital").value = cells[4].innerText;
            document.getElementById("pharmacistPosition").value = cells[5].innerText;
        } else {
            radioFormInput[0].checked = true;
        }

        document.getElementsByClassName("add_user_page-title")[1].classList.add("active-title");
        document.getElementsByClassName("add_user_page-title")[0].classList.remove("active-title");
        document.getElementsByClassName("add_user_page")[0].classList.add("user_page_active");
        document.getElementsByClassName("admin_main_content")[0].style.display = "none";
        document.getElementById("action").value = "update";
    });
});

// Search filter logic (unchanged)
function handleSearchInput(tableId, query) {
    const tableRows = document.getElementById(tableId).getElementsByTagName('tr');
    for (const row of tableRows) {
        const cells = row.getElementsByTagName('td');
        let found = false;
        for (const cell of cells) {
            const columnName = cell.getAttribute('data-column');
            if (columnName) {
                const cellContent = cell.textContent.toLowerCase().replace(/-/g, '');
                const searchQuery = query.replace(/-/g, '');
                if (cellContent.includes(searchQuery)) {
                    found = true;
                    break;
                }
            }
        }
        row.style.display = found ? 'table-row' : 'none';
    }
}

function resetTableDisplay(tableId) {
    const tableRows = document.getElementById(tableId).getElementsByTagName('tr');
    for (const row of tableRows) {
        row.style.display = 'table-row';
    }
}

document.getElementById('search-input-patient').addEventListener('input', function () {
    const query = this.value.trim().toLowerCase();
    resetTableDisplay('patientTable');
    handleSearchInput('patientTable', query);
});
document.getElementById('search-input-admin').addEventListener('input', function () {
    const query = this.value.trim().toLowerCase();
    resetTableDisplay('adminTable');
    handleSearchInput('adminTable', query);
});
document.getElementById('search-input-doctor').addEventListener('input', function () {
    const query = this.value.trim().toLowerCase();
    resetTableDisplay('doctorTable');
    handleSearchInput('doctorTable', query);
});
document.getElementById('search-input-pharmacist').addEventListener('input', function () {
    const query = this.value.trim().toLowerCase();
    resetTableDisplay('pharmacistTable');
    handleSearchInput('pharmacistTable', query);
});
