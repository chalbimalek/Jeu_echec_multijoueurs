import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { SignupComponent } from './components/signup/signup.component';
import { LobbyComponent } from './components/lobby/lobby.component';
import { GameBoardComponent } from './components/game-board/game-board.component';
import { AuthGuardService } from './guards/auth-guard.service';

const routes: Routes = [
    { path: '', component: HomeComponent },
  { path: 'connexion', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
{ path: 'lobby', component: LobbyComponent, canActivate: [AuthGuardService] },
  { path: 'game/:id', component: GameBoardComponent, canActivate: [AuthGuardService] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
