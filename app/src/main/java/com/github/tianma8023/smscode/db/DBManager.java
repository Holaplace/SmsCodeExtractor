package com.github.tianma8023.smscode.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.github.tianma8023.smscode.entity.DaoMaster;
import com.github.tianma8023.smscode.entity.DaoSession;
import com.github.tianma8023.smscode.entity.SmsCodeRule;
import com.github.tianma8023.smscode.entity.SmsCodeRuleDao;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * Database Manager for GreenDao
 */
public class DBManager {

    private static final String DB_NAME = "sms-code.db";

    private static DBManager sInstance;

    private DaoSession mDaoSession;

    private DBManager(Context context) {
        DaoMaster.DevOpenHelper dbOpenHelper =
                new DaoMaster.DevOpenHelper(context.getApplicationContext(), DB_NAME);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        mDaoSession = new DaoMaster(database).newSession();
    }

    public static DBManager get(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (DBManager.class) {
                if (sInstance == null) {
                    sInstance = new DBManager(context);
                }
            }
        }
        return sInstance;
    }

    private <T> AbstractDao getAbstractDao(Class<T> entityClass) {
        return mDaoSession.getDao(entityClass);
    }

    @SuppressWarnings("unchecked")
    private <T> long addEntity(Class<T> entityClass, T entity) {
        AbstractDao abstractDao = getAbstractDao(entityClass);
        return abstractDao.insert(entity);
    }

    @SuppressWarnings("unchecked")
    private <T> void addEntities(Class<T> entityClass, List<T> entities) {
        AbstractDao abstractDao = getAbstractDao(entityClass);
        abstractDao.insertInTx(entities);
    }

    @SuppressWarnings("unchecked")
    private <T> void updateEntity(Class<T> entityClass, T entity) {
        AbstractDao abstractDao = getAbstractDao(entityClass);
        abstractDao.update(entity);
    }

    @SuppressWarnings("unchecked")
    private <T> void removeEntity(Class<T> entityClass, T entity) {
        AbstractDao abstractDao = getAbstractDao(entityClass);
        abstractDao.delete(entity);
    }

    public long addSmsCodeRule(SmsCodeRule smsCodeRule) {
        return addEntity(SmsCodeRule.class, smsCodeRule);
    }

    public void addSmsCodeRules(List<SmsCodeRule> smsCodeRules) {
        addEntities(SmsCodeRule.class, smsCodeRules);
    }

    public void updateSmsCodeRule(SmsCodeRule smsCodeRule) {
        updateEntity(SmsCodeRule.class, smsCodeRule);
    }

    public List<SmsCodeRule> queryAllSmsCodeRules() {
        return mDaoSession.queryBuilder(SmsCodeRule.class).list();
    }

    public List<SmsCodeRule> querySmsCodeRules(SmsCodeRule criteria) {
        SmsCodeRuleDao dao = mDaoSession.getSmsCodeRuleDao();
        return dao.queryBuilder().
                where(
                        SmsCodeRuleDao.Properties.Company.eq(criteria.getCompany()),
                        SmsCodeRuleDao.Properties.CodeKeyword.eq(criteria.getCodeKeyword()),
                        SmsCodeRuleDao.Properties.CodeRegex.eq(criteria.getCodeRegex()),
                        SmsCodeRuleDao.Properties.CaseSensitive.eq(criteria.getCaseSensitive())
                ).list();
    }

    public boolean isExist(SmsCodeRule codeRule) {
        return !querySmsCodeRules(codeRule).isEmpty();
    }

    public void removeSmsCodeRule(SmsCodeRule smsCodeRule) {
        removeEntity(SmsCodeRule.class, smsCodeRule);
    }
}