<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const features = [
  {
    icon: "folder",
    title: "Soumission de demandes",
    description: "Déposez vos demandes administratives en quelques clics"
  },
  {
    icon: "clock",
    title: "Suivi en temps réel",
    description: "Suivez l'avancement de vos demandes à tout moment"
  },
  {
    icon: "shield",
    title: "Sécurité garantie",
    description: "Vos données sont protégées et confidentielles"
  },
  {
    icon: "zap",
    title: "Traitement rapide",
    description: "Une équipe dédiée pour traiter vos demandes efficacement"
  }
];
</script>

<template>
  <div class="home-page">
    <!-- Background -->
    <div class="bg-gradient"></div>
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>
    <div class="bg-orb orb-3"></div>

    <!-- Navbar -->
    <nav class="navbar">
      <div class="nav-content">
        <div class="logo">
          <div class="logo-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="url(#navGrad)" stroke-width="2"/>
              <path d="M2 17l10 5 10-5" stroke="url(#navGrad)" stroke-width="2"/>
              <path d="M2 12l10 5 10-5" stroke="url(#navGrad)" stroke-width="2"/>
              <defs><linearGradient id="navGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#06b6d4"/><stop offset="100%" style="stop-color:#8b5cf6"/>
              </linearGradient></defs>
            </svg>
          </div>
          <span class="logo-text">GestionDA</span>
        </div>
        <div class="nav-actions">
          <router-link to="/login" class="btn-login">Connexion</router-link>
          <router-link to="/register" class="btn-register">Créer un compte</router-link>
        </div>
      </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <div class="hero-badge">✨ Plateforme officielle</div>
        <h1>Gestion des Demandes<br/><span>Administratives</span></h1>
        <p>Simplifiez vos démarches administratives. Soumettez, suivez et gérez toutes vos demandes depuis une seule plateforme intuitive.</p>
        <div class="hero-actions">
          <router-link to="/register" class="btn-primary">
            Commencer maintenant
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
            </svg>
          </router-link>
          <router-link to="/login" class="btn-secondary">
            J'ai déjà un compte
          </router-link>
        </div>
      </div>
      <div class="hero-visual">
        <div class="dashboard-preview">
          <div class="preview-header">
            <div class="dots"><span></span><span></span><span></span></div>
          </div>
          <div class="preview-content">
            <div class="preview-card"></div>
            <div class="preview-card"></div>
            <div class="preview-card"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <div class="features-content">
        <div class="section-header">
          <h2>Pourquoi nous choisir ?</h2>
          <p>Notre plateforme offre une expérience simplifiée pour toutes vos demandes</p>
        </div>
        <div class="features-grid">
          <div v-for="(feature, index) in features" :key="index" class="feature-card">
            <div class="feature-icon">
              <svg v-if="feature.icon === 'folder'" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/>
              </svg>
              <svg v-else-if="feature.icon === 'clock'" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>
              </svg>
              <svg v-else-if="feature.icon === 'shield'" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
              <svg v-else width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
              </svg>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="cta-content">
        <h2>Prêt à simplifier vos démarches ?</h2>
        <p>Créez votre compte gratuitement et commencez dès maintenant</p>
        <router-link to="/register" class="btn-cta">
          Créer mon compte gratuit
        </router-link>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-logo">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="url(#footGrad)" stroke-width="2"/>
            <path d="M2 17l10 5 10-5" stroke="url(#footGrad)" stroke-width="2"/>
            <defs><linearGradient id="footGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style="stop-color:#06b6d4"/><stop offset="100%" style="stop-color:#8b5cf6"/>
            </linearGradient></defs>
          </svg>
          <span>GestionDA</span>
        </div>
        <p>© 2026 Gestion des Demandes Administratives. Tous droits réservés.</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.home-page { min-height: 100vh; position: relative; overflow-x: hidden; }

.bg-gradient { position: fixed; inset: 0; background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #0f172a 100%); z-index: -3; }
.bg-orb { position: fixed; border-radius: 50%; filter: blur(120px); opacity: 0.4; z-index: -2; animation: float 25s infinite ease-in-out; }
.orb-1 { width: 600px; height: 600px; background: linear-gradient(135deg, #06b6d4, #0891b2); top: -200px; left: -200px; }
.orb-2 { width: 700px; height: 700px; background: linear-gradient(135deg, #8b5cf6, #7c3aed); bottom: -200px; right: -200px; animation-delay: -12s; }
.orb-3 { width: 400px; height: 400px; background: linear-gradient(135deg, #ec4899, #db2777); top: 40%; right: 10%; animation-delay: -6s; }

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(40px, -40px) scale(1.1); }
  66% { transform: translate(-30px, 30px) scale(0.95); }
}

/* Navbar */
.navbar { position: fixed; top: 0; left: 0; right: 0; z-index: 100; background: rgba(15, 23, 42, 0.8); backdrop-filter: blur(16px); border-bottom: 1px solid rgba(255,255,255,0.05); }
.nav-content { max-width: 1200px; margin: 0 auto; padding: 16px 32px; display: flex; justify-content: space-between; align-items: center; }
.logo { display: flex; align-items: center; gap: 12px; }
.logo-icon { width: 48px; height: 48px; background: linear-gradient(135deg, rgba(6,182,212,0.15), rgba(139,92,246,0.15)); border-radius: 14px; display: flex; align-items: center; justify-content: center; }
.logo-text { font-size: 22px; font-weight: 800; background: linear-gradient(135deg, #06b6d4, #8b5cf6); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.nav-actions { display: flex; gap: 12px; }
.btn-login { padding: 10px 20px; color: rgba(255,255,255,0.8); text-decoration: none; font-weight: 500; font-size: 14px; transition: color 0.2s; }
.btn-login:hover { color: white; }
.btn-register { padding: 10px 24px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); color: white; text-decoration: none; border-radius: 10px; font-weight: 600; font-size: 14px; transition: all 0.2s; }
.btn-register:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(6,182,212,0.4); }

/* Hero */
.hero { min-height: 100vh; display: flex; align-items: center; padding: 120px 32px 80px; max-width: 1200px; margin: 0 auto; gap: 60px; }
.hero-content { flex: 1; }
.hero-badge { display: inline-block; background: rgba(139,92,246,0.15); color: #a78bfa; padding: 8px 16px; border-radius: 20px; font-size: 14px; font-weight: 600; margin-bottom: 24px; }
.hero h1 { color: white; font-size: 56px; font-weight: 800; line-height: 1.1; margin: 0 0 24px; }
.hero h1 span { background: linear-gradient(135deg, #06b6d4, #8b5cf6); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.hero p { color: rgba(255,255,255,0.6); font-size: 18px; line-height: 1.7; margin: 0 0 36px; max-width: 500px; }
.hero-actions { display: flex; gap: 16px; flex-wrap: wrap; }
.btn-primary { display: flex; align-items: center; gap: 10px; padding: 16px 32px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); color: white; text-decoration: none; border-radius: 14px; font-weight: 600; font-size: 16px; transition: all 0.3s; }
.btn-primary:hover { transform: translateY(-3px); box-shadow: 0 12px 30px rgba(6,182,212,0.5); }
.btn-secondary { padding: 16px 32px; background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.1); color: white; text-decoration: none; border-radius: 14px; font-weight: 500; font-size: 16px; transition: all 0.2s; }
.btn-secondary:hover { background: rgba(255,255,255,0.12); }

.hero-visual { flex: 1; display: flex; justify-content: center; }
.dashboard-preview { width: 100%; max-width: 500px; background: rgba(15,23,42,0.8); backdrop-filter: blur(20px); border: 1px solid rgba(255,255,255,0.1); border-radius: 20px; overflow: hidden; box-shadow: 0 40px 80px rgba(0,0,0,0.5); }
.preview-header { padding: 16px 20px; background: rgba(255,255,255,0.03); border-bottom: 1px solid rgba(255,255,255,0.08); }
.dots { display: flex; gap: 8px; }
.dots span { width: 12px; height: 12px; border-radius: 50%; }
.dots span:nth-child(1) { background: #ef4444; }
.dots span:nth-child(2) { background: #f59e0b; }
.dots span:nth-child(3) { background: #10b981; }
.preview-content { padding: 24px; display: flex; flex-direction: column; gap: 16px; }
.preview-card { height: 72px; background: linear-gradient(135deg, rgba(6,182,212,0.1), rgba(139,92,246,0.1)); border-radius: 12px; border: 1px solid rgba(255,255,255,0.05); }

/* Features */
.features { padding: 100px 32px; }
.features-content { max-width: 1200px; margin: 0 auto; }
.section-header { text-align: center; margin-bottom: 60px; }
.section-header h2 { color: white; font-size: 36px; font-weight: 700; margin: 0 0 16px; }
.section-header p { color: rgba(255,255,255,0.5); font-size: 18px; margin: 0; }
.features-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 24px; }
.feature-card { background: rgba(15,23,42,0.6); backdrop-filter: blur(12px); border: 1px solid rgba(255,255,255,0.08); border-radius: 20px; padding: 32px; transition: transform 0.2s, box-shadow 0.2s; }
.feature-card:hover { transform: translateY(-6px); box-shadow: 0 20px 40px rgba(0,0,0,0.3); }
.feature-icon { width: 60px; height: 60px; background: linear-gradient(135deg, rgba(6,182,212,0.15), rgba(139,92,246,0.15)); border-radius: 16px; display: flex; align-items: center; justify-content: center; margin-bottom: 20px; color: #22d3ee; }
.feature-card h3 { color: white; font-size: 20px; font-weight: 600; margin: 0 0 12px; }
.feature-card p { color: rgba(255,255,255,0.5); font-size: 15px; line-height: 1.6; margin: 0; }

/* CTA */
.cta-section { padding: 80px 32px; }
.cta-content { max-width: 700px; margin: 0 auto; text-align: center; background: linear-gradient(135deg, rgba(6,182,212,0.1), rgba(139,92,246,0.1)); border: 1px solid rgba(255,255,255,0.1); border-radius: 28px; padding: 60px 40px; }
.cta-content h2 { color: white; font-size: 32px; font-weight: 700; margin: 0 0 16px; }
.cta-content p { color: rgba(255,255,255,0.6); font-size: 18px; margin: 0 0 32px; }
.btn-cta { display: inline-block; padding: 18px 40px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); color: white; text-decoration: none; border-radius: 14px; font-weight: 600; font-size: 16px; transition: all 0.3s; }
.btn-cta:hover { transform: translateY(-3px); box-shadow: 0 12px 30px rgba(6,182,212,0.5); }

/* Footer */
.footer { padding: 40px 32px; border-top: 1px solid rgba(255,255,255,0.05); }
.footer-content { max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px; }
.footer-logo { display: flex; align-items: center; gap: 10px; }
.footer-logo span { color: white; font-weight: 600; }
.footer p { color: rgba(255,255,255,0.4); font-size: 14px; margin: 0; }

@media (max-width: 900px) {
  .hero { flex-direction: column; text-align: center; padding-top: 140px; }
  .hero p { max-width: 100%; }
  .hero-actions { justify-content: center; }
  .hero h1 { font-size: 40px; }
  .hero-visual { width: 100%; }
  .footer-content { justify-content: center; text-align: center; }
}

@media (max-width: 600px) {
  .nav-content { padding: 12px 16px; }
  .logo-icon { width: 40px; height: 40px; }
  .logo-icon svg { width: 24px; height: 24px; }
  .logo-text { font-size: 18px; }
  .nav-actions { gap: 8px; }
  .btn-login { padding: 8px 12px; font-size: 13px; }
  .btn-register { padding: 8px 14px; font-size: 13px; }
  .hero { padding: 120px 20px 60px; }
  .hero h1 { font-size: 32px; }
  .hero p { font-size: 16px; }
  .hero-actions { flex-direction: column; width: 100%; }
  .btn-primary, .btn-secondary { width: 100%; justify-content: center; padding: 14px 24px; }
  .features { padding: 60px 20px; }
  .section-header h2 { font-size: 28px; }
  .cta-section { padding: 60px 20px; }
  .cta-content { padding: 40px 24px; }
  .cta-content h2 { font-size: 24px; }
  .footer { padding: 30px 20px; }
  .footer p { font-size: 12px; }
}
</style>
