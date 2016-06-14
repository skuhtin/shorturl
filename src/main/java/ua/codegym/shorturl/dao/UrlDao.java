package ua.codegym.shorturl.dao;

import org.hibernate.SessionFactory;
import ua.codegym.shorturl.model.UrlRecord;

import static org.hibernate.criterion.Restrictions.eq;

public class UrlDao extends AbstractDao<UrlRecord>{

  public UrlDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public UrlRecord getByShortUrl(String shortUrl) {
    return singleResult(criteria().add(eq("shortUrl", shortUrl)));

  }
}
