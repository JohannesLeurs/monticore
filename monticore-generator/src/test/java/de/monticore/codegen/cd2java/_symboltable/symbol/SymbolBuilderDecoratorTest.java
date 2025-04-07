/* (c) https://github.com/MontiCore/monticore */
package de.monticore.codegen.cd2java._symboltable.symbol;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import de.monticore.cd.codegen.CD2JavaTemplates;
import de.monticore.cd.facade.CDModifier;
import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4codebasis._ast.ASTCDConstructor;
import de.monticore.cd4codebasis._ast.ASTCDMethod;
import de.monticore.cdbasis._ast.ASTCDAttribute;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.codegen.cd2java.AbstractService;
import de.monticore.codegen.cd2java.DecoratorTestCase;
import de.monticore.codegen.cd2java._ast.builder.BuilderDecorator;
import de.monticore.codegen.cd2java._symboltable.SymbolTableService;
import de.monticore.codegen.cd2java.methods.AccessorDecorator;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.types.MCTypeFacade;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static de.monticore.cd.facade.CDModifier.PUBLIC;
import static de.monticore.codegen.cd2java.DecoratorAssert.assertBoolean;
import static de.monticore.codegen.cd2java.DecoratorAssert.assertDeepEquals;
import static de.monticore.codegen.cd2java.DecoratorAssert.assertInt;
import static de.monticore.codegen.cd2java.DecoratorTestUtil.getAttributeBy;
import static de.monticore.codegen.cd2java.DecoratorTestUtil.getClassBy;
import static de.monticore.codegen.cd2java.DecoratorTestUtil.getMethodBy;
import static de.monticore.codegen.cd2java.DecoratorTestUtil.getMethodsBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SymbolBuilderDecoratorTest extends DecoratorTestCase {

  private ASTCDClass builderClass;

  private MCTypeFacade mcTypeFacade;

  private static final String ENCLOSING_SCOPE_TYPE = "de.monticore.codegen.symboltable.cdforbuilder.symbol_builder._symboltable.ISymbol_BuilderScope";

  private static final String A_NODE_TYPE_OPT = "Optional<de.monticore.codegen.symboltable.cdforbuilder.symbol_builder._ast.ASTA>";

  private static final String A_NODE_TYPE = "de.monticore.codegen.symboltable.cdforbuilder.symbol_builder._ast.ASTA";

  private static final String ACCESS_MODIFIER_TYPE = "de.monticore.symboltable.modifiers.AccessModifier";

  private static final String STEREOTYPES_COMPONENT_TYPE = "java.lang.String";

  @Before
  public void setup() {
    this.mcTypeFacade = MCTypeFacade.getInstance();

    ASTCDCompilationUnit ast = parse("de", "monticore", "codegen", "symboltable","cdForBuilder", "Symbol_Builder");
    ASTCDClass cdClass = getClassBy("A", ast);
    this.glex.setGlobalValue("service", new AbstractService(ast));

    AccessorDecorator methodDecorator = new AccessorDecorator(glex, new SymbolTableService(ast));
    BuilderDecorator builderDecorator = new BuilderDecorator(glex, methodDecorator, new SymbolTableService(ast));
    SymbolBuilderDecorator astNodeBuilderDecorator = new SymbolBuilderDecorator(glex, new SymbolTableService(ast),
            builderDecorator);
    this.builderClass = astNodeBuilderDecorator.decorate(cdClass);
  }

  @Test
  public void testClassName() {
    assertEquals("ASymbolBuilder", builderClass.getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSuperInterfacesEmpty() {
    assertFalse(builderClass.isPresentCDInterfaceUsage());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testNoSuperClass() {
    assertFalse(builderClass.isPresentCDExtendUsage());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testConstructorCount() {
    assertEquals(1, builderClass.getCDConstructorList().size());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testDefaultConstructor() {
    ASTCDConstructor cdConstructor = builderClass.getCDConstructorList().get(0);
    assertDeepEquals(PUBLIC, cdConstructor.getModifier());
    assertEquals("ASymbolBuilder", cdConstructor.getName());
    assertTrue(cdConstructor.isEmptyCDParameters());
    assertFalse(cdConstructor.isPresentCDThrowsDeclaration());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAttributes() {
    assertEquals(8, builderClass.getCDAttributeList().size());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testNameAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("name", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(String.class, astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testFullNameAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("fullName", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(String.class, astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testEnclosingScopeAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("enclosingScope", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ENCLOSING_SCOPE_TYPE),
        astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testASTNodeAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("astNode", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(A_NODE_TYPE_OPT), astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testPackageNameAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("packageName", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(String.class, astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAccessModifierAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("accessModifier", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ACCESS_MODIFIER_TYPE), astcdAttribute.getMCType());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testStereotypesAttribute() {
    ASTCDAttribute astcdAttribute = getAttributeBy("stereotypes", builderClass);
    assertDeepEquals(CDModifier.PROTECTED, astcdAttribute.getModifier());
    assertDeepEquals(mcTypeFacade.createListTypeOf(STEREOTYPES_COMPONENT_TYPE), astcdAttribute.getMCType());

    assertTrue(Log.getFindings().isEmpty());
  }


  @Test
  public void testMethods() {
    assertEquals(50, builderClass.getCDMethodList().size());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testBuildMethod() {
    ASTCDMethod method = getMethodBy("build", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbol"), method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetNameMethod() {
    ASTCDMethod method = getMethodBy("getName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(String.class, method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetFullNameMethod() {
    ASTCDMethod method = getMethodBy("getFullName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(String.class, method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetPackageNameMethod() {
    ASTCDMethod method = getMethodBy("getPackageName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(String.class, method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetEnclosingScopeNameMethod() {
    ASTCDMethod method = getMethodBy("getEnclosingScope", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ENCLOSING_SCOPE_TYPE)
        , method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetAstNodeMethod() {
    ASTCDMethod method = getMethodBy("getAstNode", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(A_NODE_TYPE)
        , method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testisPresentAstNodeMethod() {
    ASTCDMethod method = getMethodBy("isPresentAstNode", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertBoolean(method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetAccessModifierNameMethod() {
    ASTCDMethod method = getMethodBy("getAccessModifier", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ACCESS_MODIFIER_TYPE)
        , method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetNameMethod() {
    ASTCDMethod method = getMethodBy("setName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(String.class, method.getCDParameter(0).getMCType());
    assertEquals("name", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetFullNameMethod() {
    ASTCDMethod method = getMethodBy("setFullName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(String.class, method.getCDParameter(0).getMCType());
    assertEquals("fullName", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetPackageNameMethod() {
    ASTCDMethod method = getMethodBy("setPackageName", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(String.class, method.getCDParameter(0).getMCType());
    assertEquals("packageName", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetEnclosingScopeMethod() {
    ASTCDMethod method = getMethodBy("setEnclosingScope", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ENCLOSING_SCOPE_TYPE),
        method.getCDParameter(0).getMCType());
    assertEquals("enclosingScope", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetAstNodeMethod() {
    ASTCDMethod method = getMethodBy("setAstNode", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(mcTypeFacade.createQualifiedType(A_NODE_TYPE),
        method.getCDParameter(0).getMCType());
    assertEquals("astNode", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetAstNodeAbsentMethod() {
    ASTCDMethod method = getMethodBy("setAstNodeAbsent", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetAccessModifierMethod() {
    ASTCDMethod method = getMethodBy("setAccessModifier", builderClass);
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(mcTypeFacade.createQualifiedType("ASymbolBuilder"), method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(mcTypeFacade.createQualifiedType(ACCESS_MODIFIER_TYPE),
        method.getCDParameter(0).getMCType());
    assertEquals("accessModifier", method.getCDParameter(0).getName());
  
    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetStereotypesListMethod() {
    ASTCDMethod method = getMethodBy("getStereotypesList", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createListTypeOf(STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testContainsStereotypesMethod() {
    ASTCDMethod method = getMethodBy("containsStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertBoolean(method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(Object.class, method.getCDParameter(0).getMCType());
    assertEquals("element", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testContainsAllStereotypesMethod() {
    ASTCDMethod method = getMethodBy("containsAllStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertBoolean(method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createCollectionTypeOf("?"),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("collection", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testIsEmptyStereotypesMethod() {
    ASTCDMethod method = getMethodBy("isEmptyStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertBoolean(method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testIteratorStereotypesMethod() {
    ASTCDMethod method = getMethodBy("iteratorStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Iterator", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSizeStereotypesMethod() {
    ASTCDMethod method = getMethodBy("sizeStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertInt(method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testToArrayStereotypesWithInitializerMethod() {
    ASTCDMethod method = getMethodBy("toArrayStereotypes", 1, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createArrayType(STEREOTYPES_COMPONENT_TYPE, 1),
      method.getMCReturnType().getMCType()
    );

    assertDeepEquals(
      mcTypeFacade.createArrayType(STEREOTYPES_COMPONENT_TYPE, 1),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("array", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testToArrayStereotypesMethod() {
    ASTCDMethod method = getMethodBy("toArrayStereotypes", 0, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createArrayType("Object", 1),
      method.getMCReturnType().getMCType()
    );

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSpliteratorStereotypesMethod() {
    ASTCDMethod method = getMethodBy("spliteratorStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Spliterator", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testStreamStereotypesMethod() {
    ASTCDMethod method = getMethodBy("streamStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Stream", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testParallelStreamStereotypesMethod() {
    ASTCDMethod method = getMethodBy("parallelStreamStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Stream", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testGetStereotypesAtIndexMethod() {
    ASTCDMethod method = getMethodBy("getStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType(STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertInt(method.getCDParameter(0).getMCType());
    assertEquals("index", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testIndexOfStereotypesMethod() {
    ASTCDMethod method = getMethodBy("indexOfStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertInt(method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(Object.class, method.getCDParameter(0).getMCType());
    assertEquals("element", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testLastIndexOfStereotypesMethod() {
    ASTCDMethod method = getMethodBy("lastIndexOfStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertInt(method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(Object.class, method.getCDParameter(0).getMCType());
    assertEquals("element", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testEqualsStereotypesMethod() {
    ASTCDMethod method = getMethodBy("equalsStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertBoolean(method.getMCReturnType().getMCType());

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(Object.class, method.getCDParameter(0).getMCType());
    assertEquals("o", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testHashCodeStereotypesMethod() {
    ASTCDMethod method = getMethodBy("hashCodeStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertInt(method.getMCReturnType().getMCType());

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testListIteratorStereotypesMethod() {
    ASTCDMethod method = getMethodBy("listIteratorStereotypes", 0, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("ListIterator", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testListIteratorStereotypesByIndexMethod() {
    ASTCDMethod method = getMethodBy("listIteratorStereotypes", 1, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("ListIterator", STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertInt(method.getCDParameter(0).getMCType());
    assertEquals("index", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSubListStereotypesMethod() {
    ASTCDMethod method = getMethodBy("subListStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createListTypeOf(STEREOTYPES_COMPONENT_TYPE),
      method.getMCReturnType().getMCType()
    );

    assertEquals(2, method.sizeCDParameters());
    assertInt(method.getCDParameter(0).getMCType());
    assertInt(method.getCDParameter(1).getMCType());
    assertEquals("start", method.getCDParameter(0).getName());
    assertEquals("end", method.getCDParameter(1).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetListStereotypesMethod() {
    ASTCDMethod method = getMethodBy("setStereotypesList", builderClass);
    System.out.println("Type: " + method.getMCReturnType().printType());
    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createListTypeOf(STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("stereotypes", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testClearStereotypesMethod() {
    ASTCDMethod method = getMethodBy("clearStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertTrue(method.isEmptyCDParameters());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAddStereotypesMethod() {
    ASTCDMethod method = getMethodBy("addStereotypes", 1, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertDeepEquals(
      mcTypeFacade.createQualifiedType(STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("element", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAddAllStereotypesMethod() {
    ASTCDMethod method = getMethodBy("addAllStereotypes", 1, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertDeepEquals(
      mcTypeFacade.createCollectionTypeOf("? extends " + STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("collection", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testRemoveStereotypesMethod() {
    List<ASTCDMethod> methods = getMethodsBy("removeStereotypes", builderClass);
    List<ASTCDMethod> removeObjMethods = methods.stream()
      .filter(m -> m.getCDParameter(0).getMCType().deepEquals(mcTypeFacade.createQualifiedType("Object")))
      .collect(Collectors.toList());

    assertEquals(1, removeObjMethods.size());
    assertDeepEquals(PUBLIC, removeObjMethods.get(0).getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      removeObjMethods.get(0).getMCReturnType().getMCType()
    );

    assertEquals(1, removeObjMethods.get(0).sizeCDParameters());
    assertEquals("element", removeObjMethods.get(0).getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testRemoveAllStereotypesMethod() {
    ASTCDMethod method = getMethodBy("removeAllStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createCollectionTypeOf("?"),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("collection", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testRetainAllStereotypesMethod() {
    ASTCDMethod method = getMethodBy("retainAllStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createCollectionTypeOf("?"),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("collection", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testRemoveIfStereotypesMethod() {
    ASTCDMethod method = getMethodBy("removeIfStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Predicate", "? super " + STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("filter", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testForEachStereotypesMethod() {
    ASTCDMethod method = getMethodBy("forEachStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Consumer", "? super " + STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("action", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAddStereotypesAtIndexMethod() {
    ASTCDMethod method = getMethodBy("addStereotypes", 2, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertInt(method.getCDParameter(0).getMCType());
    assertEquals("index", method.getCDParameter(0).getName());
    assertDeepEquals(
      STEREOTYPES_COMPONENT_TYPE,
      method.getCDParameter(1).getMCType()
    );
    assertEquals("element", method.getCDParameter(1).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testAddAllStereotypesAtIndexMethod() {
    ASTCDMethod method = getMethodBy("addAllStereotypes", 2, builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertInt(method.getCDParameter(0).getMCType());
    assertEquals("index", method.getCDParameter(0).getName());
    assertDeepEquals(
      mcTypeFacade.createCollectionTypeOf("? extends " + STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(1).getMCType()
    );
    assertEquals("collection", method.getCDParameter(1).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testRemoveStereotypesAtIndexMethod() {
    List<ASTCDMethod> methods = getMethodsBy("removeStereotypes", builderClass);
    List<ASTCDMethod> removeByIndexMethods = methods.stream()
      .filter(m -> m.getCDParameter(0).getMCType().deepEquals(mcTypeFacade.createIntType()))
      .collect(Collectors.toList());

    assertEquals(1, removeByIndexMethods.size());
    assertDeepEquals(PUBLIC, removeByIndexMethods.get(0).getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      removeByIndexMethods.get(0).getMCReturnType().getMCType()
    );

    assertEquals(1, removeByIndexMethods.get(0).sizeCDParameters());
    assertInt(removeByIndexMethods.get(0).getCDParameter(0).getMCType());
    assertEquals("index", removeByIndexMethods.get(0).getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSetStereotypesMethod() {
    ASTCDMethod method = getMethodBy("setStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(2, method.sizeCDParameters());
    assertInt(method.getCDParameter(0).getMCType());
    assertEquals("index", method.getCDParameter(0).getName());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType(STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(1).getMCType()
    );
    assertEquals("element", method.getCDParameter(1).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testReplaceAllStereotypesMethod() {
    ASTCDMethod method = getMethodBy("replaceAllStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("UnaryOperator", STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("operator", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }

  @Test
  public void testSortStereotypesMethod() {
    ASTCDMethod method = getMethodBy("sortStereotypes", builderClass);

    assertDeepEquals(PUBLIC, method.getModifier());
    assertDeepEquals(
      mcTypeFacade.createQualifiedType("ASymbolBuilder"),
      method.getMCReturnType().getMCType()
    );

    assertEquals(1, method.sizeCDParameters());
    assertDeepEquals(
      mcTypeFacade.createBasicGenericTypeOf("Comparator", "? super " + STEREOTYPES_COMPONENT_TYPE),
      method.getCDParameter(0).getMCType()
    );
    assertEquals("comparator", method.getCDParameter(0).getName());

    assertTrue(Log.getFindings().isEmpty());
  }
  
  @Test
  public void testGeneratedCode() {
    GeneratorSetup generatorSetup = new GeneratorSetup();
    generatorSetup.setGlex(glex);
    GeneratorEngine generatorEngine = new GeneratorEngine(generatorSetup);
    CD4C.init(generatorSetup);
    StringBuilder sb = generatorEngine.generate(CD2JavaTemplates.CLASS, builderClass, packageDir);
    // test parsing
    ParserConfiguration configuration = new ParserConfiguration();
    JavaParser parser = new JavaParser(configuration);
    ParseResult parseResult = parser.parse(sb.toString());
    assertTrue(parseResult.isSuccessful());
  
    assertTrue(Log.getFindings().isEmpty());
  }
}
