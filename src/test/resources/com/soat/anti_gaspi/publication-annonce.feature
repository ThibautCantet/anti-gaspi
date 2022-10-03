# language: fr
Fonctionnalité: Publication d'une annonce

  Scénario: Annonce créée
    Etant donné l'entreprise "SOAT"
    Et le titre "3 vieux ordinateurs"
    Et la description "3 ordinateurs sous Windows 10 en bon état"
    Et l'email de contact "revendeur@donner.fr"
    Et l'adresse "20 rue des frigos, 75013 Paris"
    Et la date de disponibilité "2050-05-31"
    Et la date d'expiration le "2050-06-30"
    Quand on tente une publication d’une annonce
    Alors la publication est enregistrée et un statut est "non publiée"
