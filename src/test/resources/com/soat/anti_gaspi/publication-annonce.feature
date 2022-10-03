# language: fr
Fonctionnalité: Annonces

  Scénario: Annonces affichées
    Etant donné les annnonces sauvegardées:
      | id                                   | entreprise | titre                 | description                               | email               | adresse                        | statut      | date de disponibilité | date d'expiration |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d38 | SOAT       | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | non publiée | 2050-05-31            | 2050-06-30        |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d39 | SOAT       | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | publiée     | 2050-05-31            | 2050-06-30        |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d40 | SOAT       | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | publiée     | 2050-05-31            | 2050-06-30        |
    Quand on tente d'afficher les annonces
    Alors la publication les annonces affichées sont:
      | id                                   | entreprise | titre                 | description                               | email               | adresse                        | date de disponibilité | date d'expiration |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d39 | SOAT       | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | 2050-05-31            | 2050-06-30        |
      | 9c1845ea-a7be-4848-aba4-66ba33fd6d40 | SOAT       | Don  de 3 ordinateurs | 3 ordinateurs sous Windows 10 en bon état | revendeur@donner.fr | 20 rue des frigos, 75013 Paris | 2050-05-31            | 2050-06-30        |
