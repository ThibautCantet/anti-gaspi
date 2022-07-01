# language: fr
Fonctionnalité: Publication d'une annonce

  Scénario: Annonce créée
    Etant donné l'entreprise "SOAT"
    Et le titre "3 vieux ordinateurs"
    Et la description "3 ordinateurs sous Windows 10 en bon état"
    Et l'email de contact "revendeur@donner.fr"
    Et l'adresse "20 rue des frigos, 75013 Paris"
    Et la date de disponibilité "2022-05-31"
    Et la date d'expiration le "2022-06-30"
    Quand on tente une publication d’une annonce
    Alors la publication est enregistrée et un statut est "non publiée"
    Et un mail de confirmation est envoyé à "revendeur@donner.fr"


  Scénario: Annonce confirmée
    Etant donné les annnonces:
      | id                                   | titre                 | description                               | email               | adresse                        | statut      | date de disponibilité | date d'expiration |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d38 | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | non publiée | 2022-05-31            | 2022-06-30        |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d39 | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | non publiée | 2022-05-31            | 2022-06-30        |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d40 | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | non publiée | 2022-05-31            | 2022-06-30        |
    Quand on tente de confirmer l annonce avec l id "9c1845ea-a7be-4848-aba4-66ba33fd6d38"
    Alors la publication est au statut est "publiée"
