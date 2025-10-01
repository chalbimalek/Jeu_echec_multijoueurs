import { Component } from '@angular/core';
import { Utilisateur } from '../../models/Utilisateur';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
 users: Utilisateur[] = [];
  filteredUsers: Utilisateur[] = [];
  loading = false;
  error = '';
  searchTerm = '';

  selectedUser: any | null = null;

  newUser: any = {
    username: '',
    email: '',
    prenom:'',
    nom:'',
    password: '',

  };

  constructor(private userService: UserService,private router:Router ) {}



    addUser(): void {


      const createRequest = {
        username: this.newUser.username,
        email: this.newUser.email,
        prenom: this.newUser.prenom,
        nom: this.newUser.nom,
        password: this.newUser.password,
      };

      this.userService.createUser(createRequest).subscribe({
        next: (response) => {
          console.log('Réponse création utilisateur:', response);




          Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: 'message',
            confirmButtonText: 'OK'
         }).then(() => {
        this.router.navigate(['/connexion']); // 👈 redirection après succès
      });
        },
        error: (err) => {
          this.error = 'Erreur lors de l\'ajout de l\'utilisateur';
          console.error('Erreur:', err);
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Une erreur est survenue lors de l\'ajout de l\'utilisateur.',
            confirmButtonText: 'Fermer'
          });
        }
      });
    }
     togglePasswordVisibility(passwordInput: HTMLInputElement, toggleIcon: HTMLElement) {
    if (passwordInput.type === 'password') {
      passwordInput.type = 'text';
      toggleIcon.classList.remove('fa-eye');
      toggleIcon.classList.add('fa-eye-slash');
    } else {
      passwordInput.type = 'password';
      toggleIcon.classList.remove('fa-eye-slash');
      toggleIcon.classList.add('fa-eye');
    }
  }
    goHome() {
    window.location.href = '/';
  }
  }
